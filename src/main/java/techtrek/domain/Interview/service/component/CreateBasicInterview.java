package techtrek.domain.Interview.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.service.common.NumberCountProvider;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.Interview.dto.InterviewParserResponse;
import techtrek.domain.Interview.service.common.BasicQuestion;
import techtrek.domain.Interview.dto.InterviewResponse;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.util.*;

// 기본 질문 생성하기
@Component
@RequiredArgsConstructor
public class CreateBasicInterview {
    private final RedisTemplate<String, String> redisTemplate;
    private final UserValidator userValidator;
    private final EnterpriseRepository enterpriseRepository;
    private final BasicQuestion basicQuestion;
    private final NumberCountProvider numberCountProvider;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    public InterviewResponse.Question exec(String sessionId, CustomUserDetails userDetails){
        // 사용자 조회
        userValidator.validateAndGetUser(userDetails.getId());

        // key 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String basicKey = sessionKey + basicPrefix+ fieldId;

        // 세션 유효성 확인
        if (Boolean.FALSE.equals(redisTemplate.hasKey(sessionKey))) throw new CustomException(ErrorCode.SESSION_NOT_FOUND);

        // enterpriseName 조회 및 유효성 검증
        String enterpriseName = (String) redisTemplate.opsForHash().get(sessionKey, "enterpriseName");
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));

        // 기본 질문 생성
        InterviewParserResponse.ChatResult questionResult = basicQuestion.exec(enterprise);

        // questionNumber, count 계산
        InterviewParserResponse.NumberCount numberCount = numberCountProvider.exec(sessionKey);

        // redis 저장
        redisTemplate.opsForHash().put(basicKey, "question",  questionResult.getQuestion());
        redisTemplate.opsForHash().put(basicKey, "correctAnswer", questionResult.getCorrectAnswer());
        redisTemplate.opsForHash().put(basicKey, "questionNumber", numberCount.getQuestionNumber());
        redisTemplate.opsForHash().put(basicKey, "currentCount", numberCount.getCurrentCount());

        return InterviewResponse.Question.builder()
                .fieldId(fieldId)
                .question(questionResult.getQuestion())
                .questionNumber(numberCount.getQuestionNumber())
                .build();
    }
}
