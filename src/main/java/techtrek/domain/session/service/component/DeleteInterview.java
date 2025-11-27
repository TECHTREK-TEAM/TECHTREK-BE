package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.session.service.helper.SessionRedisHelper;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.util.Set;

// 면접 종료
@Component
@RequiredArgsConstructor
public class DeleteInterview {
    private final RedisTemplate<String, String> redisTemplate;
    private final UserValidator userValidator;
    private final SessionRedisHelper sessionRedisHelper;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    public Boolean exec(String sessionId, CustomUserDetails userDetails){
        // 사용자 조회
        userValidator.validateAndGetUser(userDetails.getId());

        String sessionKey = interviewPrefix + sessionId;
        
        // 키가 없으면 예외
        sessionRedisHelper.validateSession(sessionKey);

        // Redis 데이터 삭제
        Set<String> keys = redisTemplate.keys(interviewPrefix + sessionId + "*");
        if (keys != null && !keys.isEmpty()) redisTemplate.delete(keys);

        return true;
    }
}

