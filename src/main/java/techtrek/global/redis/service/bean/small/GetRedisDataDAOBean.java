package techtrek.global.redis.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetRedisDataDAOBean {
    private final RedisTemplate<String, String> redisTemplate;

    // 전체 번호
    public String exec(String sessionKey){
        Long value = redisTemplate.opsForHash().size(sessionKey);
        String totalQuestionNumber = String.valueOf(value + 1);

        return totalQuestionNumber;

    }

    // 번호
    public String exec(String sessionKey, String fieldId){
        String fieldKey = sessionKey + ":" + fieldId;

        Object value = redisTemplate.opsForHash().get(fieldKey, "questionNumber");
        String questionNumber = value != null ? value.toString() : "1";

        return questionNumber;
    }
}
