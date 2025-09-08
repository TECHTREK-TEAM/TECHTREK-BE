package techtrek.global.redis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckRedisKeyDAO {
    private final RedisTemplate<String, String> redisTemplate;

    // key가 존재하는지 확인
    public boolean exec(String key) {
        return redisTemplate.hasKey(key);
    }
}
