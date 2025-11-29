package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import techtrek.domain.analysis.dto.AnalysisParserResponse;
import techtrek.domain.questionAnswer.entity.QuestionAnswer;
import techtrek.domain.questionAnswer.repository.QuestionAnswerRepository;
import techtrek.domain.session.service.helper.CompanyCSHelper;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.analysis.service.helper.LowestSimilarityHelper;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.session.service.helper.SessionRedisHelper;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.openAI.chat.service.common.Gpt;
import techtrek.global.securty.service.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateAnalysis {
    private static final String PROMPT_PATH_FEEDBACK = "prompts/feedback_prompt.txt";

    private final RedisTemplate<String, String> redisTemplate;
    private final LowestSimilarityHelper lowestSimilarityHelper;
    private final SessionRedisHelper sessionRedisHelper;
    private final CompanyCSHelper companyCSHelper;
    private final UserHelper userHelper;

    private final AnalysisRepository analysisRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final Gpt gpt;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.qa}")
    private String qaPrefix;


    // 분석하기
    @Transactional
    public AnalysisResponse.Analysis exec(String sessionId, int duration, CustomUserDetails userDetails) {
        // 사용자 조회
        User user = userHelper.validateUser(userDetails.getId());

        // 키 없으면 예외
        String sessionKey = interviewPrefix + sessionId;
        sessionRedisHelper.validateSession(sessionKey);

        // enterpriseName 조회
        Enterprise enterprise = sessionRedisHelper.getEnterprise(sessionKey);

        // 분석 엔티티 생성
        Analysis analysis = createAnalysisEntity(user, enterprise, sessionId, duration);

        // Redis QA 읽어서 DB 저장
        saveQaFromRedis(sessionKey, analysis);

        // Redis 삭제
        Set<String> sessionKeys = redisTemplate.keys(sessionKey + "*");
        if (sessionKeys != null && !sessionKeys.isEmpty()) redisTemplate.delete(sessionKeys);

        // 최소 유사도 QA 조회
        AnalysisParserResponse.LowestSimilarity low = lowestSimilarityHelper.getLowestSimilarity(analysis);

        // 전체 유사도 점수 계산
        AnalysisParserResponse.AnalysisResult res = calculatePassAndRate(analysis);
        boolean isPass = res.isPass();
        double score = res.getPercentage();

        // 기업별 CS와 GPT 피드백 생성
        String focusCS = companyCSHelper.exec(enterprise.getName());
        AnalysisParserResponse.feedbackResult result = gpt.exec(
                PROMPT_PATH_FEEDBACK,
                new Object[]{enterprise.getName(), focusCS,low.getQuestion(), low.getAnswer(),low.getSimilarity() },
                AnalysisParserResponse.feedbackResult.class
        );

        // 분석 테이블 업데이트
        analysis.updateResult(isPass, score, result.getKeyword(), low.getQuestionNumber(), result.getFeedback(), user.getPosition());

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


    // 분석 엔티티 생성
    private Analysis createAnalysisEntity(User user, Enterprise enterprise, String sessionId, int duration) {
        Analysis analysis = Analysis.builder()
                .enterprise(enterprise)
                .user(user)
                .sessionId(sessionId)
                .createdAt(LocalDateTime.now())
                .duration(duration)
                .score(0.0)
                .isPass(false)
                .keyword("")
                .keywordNumber("0")
                .feedback("")
                .analysisPosition(user.getPosition())
                .build();
        return analysisRepository.save(analysis);
    }


    // DB에서 QA 읽어서 DB 저장, redis 삭제
    private void saveQaFromRedis(String sessionKey, Analysis analysis) {
        String pattern = sessionKey + qaPrefix + "*";
        Set<String> keys = redisTemplate.keys(pattern);

        if (keys == null || keys.isEmpty()) {
            throw new CustomException(ErrorCode.QA_NOT_FOUND);
        }

        for (String key : keys) {
            Map<Object, Object> map = redisTemplate.opsForHash().entries(key);
            QuestionAnswer qa = mapToQuestionAnswer(map, analysis, key);
            questionAnswerRepository.save(qa);
        }
    }

    // redis hash 데이터를 question answer 엔티티로 변환
    private QuestionAnswer mapToQuestionAnswer(Map<Object, Object> map, Analysis analysis, String key) {
        String[] tokens = key.split(":");
        int mainNumber = 0;
        int subNumber = 0;

        try {
            if (tokens.length >= 5) {
                mainNumber = Integer.parseInt(tokens[tokens.length - 2]);
                subNumber = Integer.parseInt(tokens[tokens.length - 1]);
            } else {
                mainNumber = Integer.parseInt(tokens[tokens.length - 1]);
            }
        } catch (NumberFormatException ignored) {}

        String question = (String) map.get("question");
        String correctAnswer = (String) map.get("correctAnswer");
        String answer = (String) map.getOrDefault("answer", "");
        String type = (String) map.get("type");
        String similarityStr = (String) map.get("similarity");

        double similarity =  Double.parseDouble(similarityStr);

        return QuestionAnswer.builder()
                .analysis(analysis)
                .type(type)
                .question(question)
                .correctAnswer(correctAnswer)
                .answer(answer)
                .similarity(similarity)
                .mainNumber(mainNumber)
                .subNumber(subNumber)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // 일치율, 합격률 계산
    public AnalysisParserResponse.AnalysisResult calculatePassAndRate(Analysis analysis) {
        // DB에서 직접 조회
        List<QuestionAnswer> qaList = questionAnswerRepository.findByAnalysis(analysis);

        if (qaList.isEmpty()) return new AnalysisParserResponse.AnalysisResult(false, 0.0);

        double total = qaList.stream()
                .mapToDouble(QuestionAnswer::getSimilarity)
                .sum();

        double avg = total / qaList.size();  // 평균 similarity

        boolean isPass = avg >= 0.6;  // 60% 기준

        double percentage = avg * 100; // 퍼센트 변환

        // 소수점 둘째 자리에서 반올림
        percentage = Math.round(percentage * 100.0) / 100.0;

        return new AnalysisParserResponse.AnalysisResult(isPass, percentage);
    }



}