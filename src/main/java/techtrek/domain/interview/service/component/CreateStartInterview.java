package techtrek.domain.interview.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.interview.dto.BasicQuestionResponse;
import techtrek.domain.interview.dto.SessionInfoResponse;
import techtrek.domain.interview.service.common.BasicQuestion;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.*;

// 면접 시작하기
@Component
@RequiredArgsConstructor
public class CreateStartInterview {
    private static final String START_QUESTION_NUMBER = "1";

    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestion basicQuestion;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.basic}")
    private String basicPrefix;

    // 면접 시작하기
    public SessionInfoResponse.Start exec(String enterpriseName){
        // TODO: 토큰에서 userId 꺼내고 해당 userId로 사용자 조회 (하나의 컴포넌트로 빼두기)
        userRepository.findById("1")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 세션 생성
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String basicKey = sessionKey +basicPrefix+ fieldId;

        // 기업 존재 확인
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NAME_NOT_FOUND));

        // 기본 질문 생성
        BasicQuestionResponse.BasicQuestionResult basicQuestionResult = basicQuestion.exec(enterprise);

        // redis 저장
        redisTemplate.opsForHash().put(sessionKey, "enterpriseName", enterpriseName);
        redisTemplate.opsForHash().put(basicKey, "question",  basicQuestionResult.getQuestion());
        redisTemplate.opsForHash().put(basicKey, "correctAnswer", basicQuestionResult.getCorrectAnswer());
        redisTemplate.opsForHash().put(basicKey, " questionNumber", START_QUESTION_NUMBER);

        return SessionInfoResponse.Start.builder()
                .sessionId(sessionId)
                .fieldId(fieldId)
                .question(basicQuestionResult.getQuestion())
                .questionNumber(START_QUESTION_NUMBER)
                .build();
    }
}