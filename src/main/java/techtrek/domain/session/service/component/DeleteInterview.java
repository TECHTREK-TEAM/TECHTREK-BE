package techtrek.domain.session.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
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

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    public Boolean exec(String sessionId, CustomUserDetails userDetails){
        // 사용자 조회
        userValidator.validateAndGetUser(userDetails.getId());

        // 세션 유효성 확인
        if (Boolean.FALSE.equals(redisTemplate.hasKey(interviewPrefix + sessionId))) throw new CustomException(ErrorCode.SESSION_NOT_FOUND);

        // Redis 데이터 삭제
        Set<String> keys = redisTemplate.keys(interviewPrefix + sessionId + "*");
        if (keys != null && !keys.isEmpty()) redisTemplate.delete(keys);

        return true;
    }
}

