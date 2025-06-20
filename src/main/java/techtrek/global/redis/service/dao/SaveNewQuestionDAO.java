package techtrek.global.redis.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveNewQuestionDAO {
    private final RedisTemplate<String, String> redisTemplate;

    // 새로운 질문 RedisDTO
    public void exec(String newKey, String phase, String count, String basicQuestion, String questionNumber,String totalQuestionNumber ) {
        redisTemplate.opsForHash().put(newKey, "phase", phase);
        redisTemplate.opsForHash().put(newKey, "count", count);
        redisTemplate.opsForHash().put(newKey, "question", basicQuestion);
        redisTemplate.opsForHash().put(newKey, "questionNumber", questionNumber);
        redisTemplate.opsForHash().put(newKey, "totalQuestionNumber", totalQuestionNumber);
    }
}
