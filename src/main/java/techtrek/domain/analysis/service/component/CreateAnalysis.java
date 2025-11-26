package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import techtrek.domain.Interview.dto.InterviewParserResponse;
import techtrek.domain.Interview.service.common.CompanyCSProvider;
import techtrek.domain.Interview.service.common.NumberCountProvider;
import techtrek.domain.analysis.dto.AnalysisParserResponse;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.analysis.service.common.LowestSimilarity;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.user.entity.User;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.common.Gpt;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class CreateAnalysis {
    private static final String PROMPT_PATH_FEEDBACK = "prompts/feedback_prompt.txt";

    private final RedisTemplate<String, String> redisTemplate;
    private final UserValidator userValidator;
    private final EnterpriseRepository enterpriseRepository;
    private final AnalysisRepository analysisRepository;
    private final LowestSimilarity lowestSimilarity;
    private final NumberCountProvider numberCountProvider;
    private final CompanyCSProvider companyCSProvider;
    private final Gpt createGpt;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    @Value("${custom.redis.prefix.resume}")
    private String resumePrefix;

    @Value("${custom.redis.prefix.tail}")
    private String tailPrefix;

    // 분석하기
    @Transactional
    public AnalysisResponse.Analysis exec(String sessionId, int duration, CustomUserDetails userDetails){
        // key 생성
        String sessionKey = interviewPrefix + sessionId;
        String basicKey = sessionKey + basicPrefix;
        String resumeKey = sessionKey + resumePrefix;
        String tailKey = sessionKey + tailPrefix;

        // 사용자 조회, 유효성 확인
        User user = userValidator.validateAndGetUser(userDetails.getId());
        if (Boolean.FALSE.equals(redisTemplate.hasKey(sessionKey))) throw new CustomException(ErrorCode.SESSION_NOT_FOUND);

        // 기업불러오기
        String enterpriseName = (String) redisTemplate.opsForHash().get(sessionKey, "enterpriseName");
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName).orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));

        // 합격여부, 일치율 계산 (유사도 0.6이상 개수 * 100 / 전체개수)
        InterviewParserResponse.NumberCount numberCount = numberCountProvider.exec(sessionKey);
        long highCount = Stream.of(basicKey+"*", resumeKey+"*", tailKey+"*")
                .mapToLong(this::countHighSimilarity)
                .sum();

        double score = numberCount.getTotalCount() > 0 ? Math.round((highCount * 100.0 / numberCount.getTotalCount()) * 10) / 10.0 : 0.0;
        boolean isPass = score >= 70.0;

        // 유사도 낮은 필드 조회
        AnalysisParserResponse.LowestSimilarity low = lowestSimilarity.exec(sessionKey);

        // 기업별 중점 CS
        String focusCS = companyCSProvider.exec(enterprise.getName());

        // gpt 피드백 생성
        AnalysisParserResponse.feedbackResult result = createGpt.exec(
                PROMPT_PATH_FEEDBACK,
                new Object[]{enterprise.getName(), focusCS,low.getQuestion(), low.getAnswer(),low.getSimilarity() },
                AnalysisParserResponse.feedbackResult.class
        );

        // 분석 테이블 생성
        Analysis analysis = Analysis.builder()
                .sessionId(sessionId)
                .isPass(isPass)
                .score(score)
                .keyword(result.getKeyword())
                .keywordNumber(low.getQuestionNumber())
                .feedback(result.getFeedback())
                .analysisPosition(user.getPosition())
                .duration(duration)
                .createdAt(LocalDateTime.now().withNano(0))
                .user(user)
                .enterprise(enterprise)
                .build();

        analysisRepository.save(analysis);

        // 반환
        return AnalysisResponse.Analysis.builder()
                .analysisId(analysis.getId())
                .isPass(isPass)
                .score(score)
                .feedback(result.getFeedback())
                .duration(duration)
                .keyword(result.getKeyword())
                .build();
    }


    // similarity >= 0.6인 필드 개수 계산
    public long countHighSimilarity(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys == null || keys.isEmpty()) return 0;

        return keys.stream()
                .map(key -> redisTemplate.opsForHash().get(key, "similarity"))
                .filter(Objects::nonNull)
                .mapToDouble(v -> {
                    try { return Double.parseDouble(v.toString()); }
                    catch (NumberFormatException e) { return 0.0; }
                })
                .filter(sim -> sim >= 0.6)
                .count();
    }

}