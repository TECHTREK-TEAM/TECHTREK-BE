package techtrek.global.redis.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GetTailNumberDAO {
    private final RedisTemplate<String, String> redisTemplate;

    // 꼬리질문 번호
    public String exec(String sessionKey, String parentQuestionNumber){
        String tailCountKey = sessionKey + ":count:" + parentQuestionNumber;
        Long tailQuestionNumber = redisTemplate.opsForValue().increment(tailCountKey);
        String resultTailQuestionNumber= String.valueOf(tailQuestionNumber);

        return resultTailQuestionNumber;
    }
}
