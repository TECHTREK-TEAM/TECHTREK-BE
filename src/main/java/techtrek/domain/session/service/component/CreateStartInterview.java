package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.session.dto.SessionParserResponse;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.service.helper.BasicQuestionHelper;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;

import java.util.*;

// 면접 시작하기
@Component
@RequiredArgsConstructor
public class CreateStartInterview {
    private static final String START_QUESTION_NUMBER = "1";

    private final RedisTemplate<String, String> redisTemplate;
    private final BasicQuestionHelper basicQuestionHelper;
    private final UserHelper userHelper;

    private final EnterpriseRepository enterpriseRepository;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.qa}")
    private String qaPrefix;

    // 면접 시작하기
    public SessionResponse.Start exec(String enterpriseName, CustomUserDetails userDetails){
        // 사용자 조회
        User user = userHelper.validateUser(userDetails.getId());

        // 세션 키
        String sessionId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String qaKey = interviewPrefix + sessionId + qaPrefix + START_QUESTION_NUMBER;

        // enterpriseName 유효성 검증
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName).orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));

        // 기본 질문 생성
        SessionParserResponse.ChatResult questionResult = basicQuestionHelper.exec(enterprise);

        // redis 저장
        redisTemplate.opsForHash().put(sessionKey, "userId", user.getId());
        redisTemplate.opsForHash().put(sessionKey, "enterpriseName", enterpriseName);
        redisTemplate.opsForHash().put(sessionKey, "mainNumber", START_QUESTION_NUMBER);
        redisTemplate.opsForHash().put(sessionKey, "subNumber", "0");
        redisTemplate.opsForHash().put(sessionKey, "currentCount", START_QUESTION_NUMBER);
        redisTemplate.opsForHash().put(qaKey, "type", "basic");
        redisTemplate.opsForHash().put(qaKey, "question",  questionResult.getQuestion());
        redisTemplate.opsForHash().put(qaKey, "correctAnswer", questionResult.getCorrectAnswer());

        return SessionResponse.Start.builder()
                .sessionId(sessionId)
                .question(questionResult.getQuestion())
                .questionNumber(START_QUESTION_NUMBER)
                .resumeStatus(user.getResume() != null)
                .build();
    }

}