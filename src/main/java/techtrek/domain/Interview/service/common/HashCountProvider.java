package techtrek.domain.Interview.service.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

// hash 개수 계산
@Component
public class HashCountProvider {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public long exec(String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        return keys != null ? keys.size() : 0;
    }
}