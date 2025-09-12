package techtrek.domain.Interview.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.Interview.dto.InterviewParserResponse;
import techtrek.domain.Interview.dto.InterviewResponse;
import techtrek.domain.Interview.service.common.BasicQuestion;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.*;

// 면접 시작하기
@Component
@RequiredArgsConstructor
public class CreateStartInterview {
    private static final String START_QUESTION_NUMBER = "1";
    private static final String CURRENT_COUNT = "1";

    private final EnterpriseRepository enterpriseRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestion basicQuestion;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    // 면접 시작하기
    public InterviewResponse.Start exec(String enterpriseName){
        // 세션 생성
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();

        String sessionKey = interviewPrefix + sessionId;
        String basicKey = sessionKey + basicPrefix+ fieldId;

        // enterpriseName 유효성 검증
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName).orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));

        // 기본 질문 생성
        InterviewParserResponse.ChatResult questionResult = basicQuestion.exec(enterprise);

        // redis 저장
        redisTemplate.opsForHash().put(sessionKey, "enterpriseName", enterpriseName);
        redisTemplate.opsForHash().put(basicKey, "question",  questionResult.getQuestion());
        redisTemplate.opsForHash().put(basicKey, "correctAnswer", questionResult.getCorrectAnswer());
        redisTemplate.opsForHash().put(basicKey, "questionNumber", START_QUESTION_NUMBER);
        redisTemplate.opsForHash().put(basicKey, "currentCount", CURRENT_COUNT);

        return InterviewResponse.Start.builder()
                .sessionId(sessionId)
                .fieldId(fieldId)
                .question(questionResult.getQuestion())
                .questionNumber(START_QUESTION_NUMBER)
                .build();
    }
}