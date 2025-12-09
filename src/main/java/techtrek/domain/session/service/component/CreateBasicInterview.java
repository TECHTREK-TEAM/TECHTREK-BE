package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.session.dto.SessionParserResponse;
import techtrek.domain.session.service.helper.BasicQuestionHelper;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.service.helper.SessionRedisHelper;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.securty.service.CustomUserDetails;

// 기본 질문 생성하기
@Component
@RequiredArgsConstructor
public class CreateBasicInterview {
    private final RedisTemplate<String, String> redisTemplate;
    private final SessionRedisHelper sessionRedisHelper;
    private final BasicQuestionHelper basicQuestionHelper;
    private final UserHelper userHelper;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    @Value("${custom.redis.prefix.qa}")
    private String qaPrefix;

    public SessionResponse.Question exec(String sessionId, CustomUserDetails userDetails){
        // 사용자 검증
        userHelper.validateUser(userDetails.getId());

        String sessionKey = interviewPrefix + sessionId;

        // 키가 없으면 예외
        sessionRedisHelper.validateSession(sessionKey);

        // Redis 값 조회
        int mainNumber = sessionRedisHelper.getIntField(sessionKey, "mainNumber");
        int currentCount = sessionRedisHelper.getIntField(sessionKey, "currentCount");

        // 다음 질문 번호
        int nextMainNumber = mainNumber + 1;
        int nextCurrentCount = currentCount + 1;

        // QA 키 생성
        String qaKey = sessionRedisHelper.buildQaKey(sessionId, nextMainNumber,0);

        // enterpriseName 조회 및 유효성 검증
        Enterprise enterprise = sessionRedisHelper.getEnterprise(sessionKey);

        // 기본 질문 생성
        SessionParserResponse.ChatResult questionResult = basicQuestionHelper.exec(enterprise);

        // Redis 저장
        saveUpdataData(sessionKey, nextMainNumber, nextCurrentCount);
        saveQaData(qaKey, questionResult);

        return SessionResponse.Question.builder()
                .question(questionResult.getQuestion())
                .questionNumber(String.valueOf(nextMainNumber))
                .build();
    }

    // redis 저장
    private void saveUpdataData(String sessionKey, int mainNumber, int currentCount) {
        redisTemplate.opsForHash().put(sessionKey, "mainNumber", String.valueOf(mainNumber));
        redisTemplate.opsForHash().put(sessionKey, "subNumber", "0");
        redisTemplate.opsForHash().put(sessionKey, "currentCount", String.valueOf(currentCount));
    }

    private void saveQaData(String qaKey, SessionParserResponse.ChatResult result) {
        redisTemplate.opsForHash().put(qaKey, "question", result.getQuestion());
        redisTemplate.opsForHash().put(qaKey, "correctAnswer", result.getCorrectAnswer());
        redisTemplate.opsForHash().put(qaKey, "type", "basic");
    }
}
