package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetRedisSessionListDAOBean {
    private final RedisTemplate<String, String> redisTemplate;

    // redis new 세션 리스트 불러오기
    public List<String> exec(String sessionKey){
        List<String> sessionData = redisTemplate.opsForList().range(sessionKey, 0, -1);
        if (sessionData == null || sessionData.isEmpty()) throw new CustomException(ErrorCode.SESSION_NOT_FOUND);

        return sessionData;
    }
}
