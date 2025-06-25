package techtrek.domain.redis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeleteRedisDAO {
    private final RedisTemplate<String, Object> redisTemplate;

    // redis 삭제
    public void exec(String redisKey){
        Set<String> keys = redisTemplate.keys(redisKey);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }
}
