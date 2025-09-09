package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.session.dto.BasicQuestionResponse;
import techtrek.domain.session.service.common.BasicQuestion;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.service.common.RedisHashCount;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.*;

// 기본 질문 생성하기
@Component
@RequiredArgsConstructor
public class CreateBasicInterview {
    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestion basicQuestion;
    private final RedisHashCount redisHashCount;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    @Value("${custom.redis.prefix.resume}")
    private String resumePrefix;

    public SessionResponse.Question exec(String sessionId){
        // TODO: 사용자 조회
        userRepository.findById("1")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // fieldId 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String basicKey = sessionKey + basicPrefix+ fieldId;

        // enterpriseName 조회 및 유효성 검증
        String enterpriseName = (String) redisTemplate.opsForHash().get(sessionKey, "enterpriseName");
        System.out.println(enterpriseName);
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NAME_NOT_FOUND));

        // 기본 질문 생성
        BasicQuestionResponse.BasicQuestionResult basicQuestionResult = basicQuestion.exec(enterprise);

        // basic + resume 필드 개수 세기
        long basicCount = redisHashCount.exec(sessionKey + basicPrefix + "*");
        long resumeCount = redisHashCount.exec(sessionKey + resumePrefix + "*");
        String questionNumber = String.valueOf(basicCount + resumeCount + 1);

        // redis 저장
        redisTemplate.opsForHash().put(basicKey, "question",  basicQuestionResult.getQuestion());
        redisTemplate.opsForHash().put(basicKey, "correctAnswer", basicQuestionResult.getCorrectAnswer());
        redisTemplate.opsForHash().put(basicKey, "questionNumber", questionNumber);

        return SessionResponse.Question.builder()
                .fieldId(fieldId)
                .question(basicQuestionResult.getQuestion())
                .questionNumber(questionNumber)
                .build();
    }
}
