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
import techtrek.domain.analysis.service.common.LowestSimilarity;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
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
    private final SessionRedisHelper sessionRedisHelper;
    private final UserHelper userHelper;
    private final AnalysisRepository analysisRepository;
    private final QuestionAnswerRepository questionAnswerRepository;
    private final LowestSimilarity lowestSimilarity;
    private final CompanyCSHelper companyCSProvider;
    private final Gpt createGpt;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.qa}")
    private String qaPrefix;


    // 분석하기
    @Transactional
    public AnalysisResponse.Analysis exec(String sessionId, int duration, CustomUserDetails userDetails) {
        // 사용자 조회
        User user = userHelper.validateUser(userDetails.getId());

        String sessionKey = interviewPrefix + sessionId;

        // 키가 없으면 예외
        sessionRedisHelper.validateSession(sessionKey);

        // enterpriseName 조회 및 유효성 검증
        Enterprise enterprise = sessionRedisHelper.getEnterprise(sessionKey);

        // Analysis 생성 및 저장
        Analysis analysis = Analysis.builder()
                .enterprise(enterprise)
                .user(user)
                .createdAt(LocalDateTime.now())
                .duration(duration)
                .isPass(false)
                .score(0.0)
                .keyword("")
                .keywordNumber("0")
                .feedback("")
                .analysisPosition(user.getPosition())
                .build();
        analysisRepository.save(analysis);

        // Redis QA 키 전체 조회
        String qaPattern = interviewPrefix + sessionId  + qaPrefix +"*";
        Set<String> keys = redisTemplate.keys(qaPattern);


        if (keys == null || keys.isEmpty()) {
            throw new CustomException(ErrorCode.QA_NOT_FOUND);
        }

        for (String key : keys) {

            // mainNumber / subNumber 추출
            String[] tokens = key.split(":");
            int mainNumber = 0;
            int subNumber = 0;

            try {
                if (tokens.length >= 5) {
                    // qa:메인:서브 구조
                    mainNumber = Integer.parseInt(tokens[tokens.length - 2]);
                    subNumber = Integer.parseInt(tokens[tokens.length - 1]);
                } else {
                    // qa:메인 구조
                    mainNumber = Integer.parseInt(tokens[tokens.length - 1]);
                    subNumber = 0;
                }
            } catch (NumberFormatException e) {
                mainNumber = 0;
                subNumber = 0;
            }

            // Redis Hash 읽기
            Map<Object, Object> map = redisTemplate.opsForHash().entries(key);

            String question = (String) map.get("question");
            String correctAnswer = (String) map.get("correctAnswer");
            String answer = (String) map.getOrDefault("answer", "");
            String type = (String) map.get("type");
            String similarityStr = Objects.toString(map.get("similarity"), "0.0").trim();
            double similarity = Double.parseDouble(similarityStr);

            // DB 저장
            QuestionAnswer qa = QuestionAnswer.builder()
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

            questionAnswerRepository.save(qa);
        }

        // Redis 데이터 삭제
        Set<String> sessionKeys = redisTemplate.keys(sessionKey + "*");
        if (sessionKeys != null && !sessionKeys.isEmpty()) redisTemplate.delete(sessionKeys);


        // 유사도 낮은 필드 조회
        AnalysisParserResponse.LowestSimilarity low = lowestSimilarity.getLowestSimilarity(analysis);

        // 10. 전체 유사도 기반 점수 및 합격 여부 계산
        AnalysisParserResponse.AnalysisResult res = calculatePassAndRate(analysis);
        boolean isPass = res.isPass();
        double score = res.getPercentage();

        System.out.println(isPass);
        System.out.println(score);

        // 기업별 중점 CS
        String focusCS = companyCSProvider.exec(enterprise.getName());

        // gpt 피드백 생성
        AnalysisParserResponse.feedbackResult result = createGpt.exec(
                PROMPT_PATH_FEEDBACK,
                new Object[]{enterprise.getName(), focusCS,low.getQuestion(), low.getAnswer(),low.getSimilarity() },
                AnalysisParserResponse.feedbackResult.class
        );

        // 분석 테이블 업데이트
        analysis.setPass(isPass);
        analysis.setScore(score);
        analysis.setKeyword(result.getKeyword());
        analysis.setKeywordNumber(low.getQuestionNumber());
        analysis.setFeedback(result.getFeedback());
        analysis.setAnalysisPosition(user.getPosition());

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

    public AnalysisParserResponse.AnalysisResult calculatePassAndRate(Analysis analysis) {
        // DB에서 직접 조회
        List<QuestionAnswer> qaList = questionAnswerRepository.findByAnalysis(analysis);

        if (qaList.isEmpty()) {
            return new AnalysisParserResponse.AnalysisResult(false, 0.0);
        }

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