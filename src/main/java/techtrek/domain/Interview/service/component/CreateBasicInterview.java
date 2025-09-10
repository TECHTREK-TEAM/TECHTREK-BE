package techtrek.domain.Interview.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.Interview.dto.ParserResponse;
import techtrek.domain.Interview.service.common.BasicQuestion;
import techtrek.domain.Interview.dto.InterviewResponse;
import techtrek.domain.Interview.service.common.HashCountProvider;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.*;

// 기본 질문 생성하기
@Component
@RequiredArgsConstructor
public class CreateBasicInterview {
    private final EnterpriseRepository enterpriseRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestion basicQuestion;
    private final HashCountProvider hashCountProvider;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    @Value("${custom.redis.prefix.resume}")
    private String resumePrefix;

    public InterviewResponse.Question exec(String sessionId){
        // key 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String basicKey = sessionKey + basicPrefix+ fieldId;

        // 세션 유효성 확인
        Boolean hasSession = redisTemplate.hasKey(sessionKey);
        if (hasSession == null || !hasSession) throw new CustomException(ErrorCode.SESSION_NOT_FOUND);

        // enterpriseName 조회 및 유효성 검증
        String enterpriseName = (String) redisTemplate.opsForHash().get(sessionKey, "enterpriseName");
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NAME_NOT_FOUND));

        // 기본 질문 생성
        ParserResponse.ChatResult questionResult = basicQuestion.exec(enterprise);

        // basic + resume 필드 개수 세기
        long basicCount = hashCountProvider.exec(sessionKey + basicPrefix + "*");
        long resumeCount = hashCountProvider.exec(sessionKey + resumePrefix + "*");
        String questionNumber = String.valueOf(basicCount + resumeCount + 1);

        // redis 저장
        redisTemplate.opsForHash().put(basicKey, "question",  questionResult.getQuestion());
        redisTemplate.opsForHash().put(basicKey, "correctAnswer", questionResult.getCorrectAnswer());
        redisTemplate.opsForHash().put(basicKey, "questionNumber", questionNumber);

        return InterviewResponse.Question.builder()
                .fieldId(fieldId)
                .question(questionResult.getQuestion())
                .questionNumber(questionNumber)
                .build();
    }
}
