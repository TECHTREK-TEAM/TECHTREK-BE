package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.session.dto.SessionParserResponse;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.service.common.BasicQuestion;
import techtrek.domain.user.entity.User;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.time.LocalDateTime;
import java.util.*;

// 면접 시작하기
@Component
@RequiredArgsConstructor
public class CreateStartInterview {
    private static final String START_QUESTION_NUMBER = "1";
    // private static final String CURRENT_COUNT = "1";

    private final UserValidator userValidator;
    private final EnterpriseRepository enterpriseRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestion basicQuestion;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.state}")
    private String state;

    @Value("${custom.redis.prefix.qa}")
    private String qa;

//    @Value("${custom.redis.prefix.basic}")
//    private String basicPrefix;

    // 면접 시작하기
    public SessionResponse.Start exec(String enterpriseName, CustomUserDetails userDetails){
        // 사용자 조회
        User user = userValidator.validateAndGetUser(userDetails.getId());

        // 세션 생성
        String sessionId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String qaKey = interviewPrefix + sessionId + qa + START_QUESTION_NUMBER;

        // 세션 생성
//        String sessionId = UUID.randomUUID().toString();
//        String fieldId = UUID.randomUUID().toString();

//        String sessionKey = interviewPrefix + sessionId;
//        String basicKey = sessionKey + basicPrefix+ fieldId;

        // enterpriseName 유효성 검증
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName).orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));

        // 기본 질문 생성
        SessionParserResponse.ChatResult questionResult = basicQuestion.exec(enterprise);

        // redis 저장
        redisTemplate.opsForHash().put(sessionKey, "userId", user.getId());
        redisTemplate.opsForHash().put(sessionKey, "enterpriseName", enterpriseName);
        redisTemplate.opsForHash().put(sessionKey, "mainNumber", START_QUESTION_NUMBER);
        redisTemplate.opsForHash().put(qaKey, "type", "basic");
        redisTemplate.opsForHash().put(qaKey, "question",  questionResult.getQuestion());
        redisTemplate.opsForHash().put(qaKey, "correctAnswer", questionResult.getCorrectAnswer());
//        redisTemplate.opsForHash().put(questionKey, "questionNumber", START_QUESTION_NUMBER);
//        redisTemplate.opsForHash().put(questionKey, "currentCount", CURRENT_COUNT);

        return SessionResponse.Start.builder()
                .sessionId(sessionId)
                .question(questionResult.getQuestion())
                .questionNumber(START_QUESTION_NUMBER)
                .resumeStatus(user.getResume() != null)
                .build();
    }
}