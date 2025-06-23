package techtrek.domain.redis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveTailQuestionDAO {
    private final RedisTemplate<String, String> redisTemplate;

    // 꼬리 질문 RedisDTO
    public void exec(String fieldKey, String question, String parentQuestionNumber, String tailQuestionNumber, String questionNumber, String totalQuestionNumber ) {
        redisTemplate.opsForHash().put(fieldKey, "question", question);
        redisTemplate.opsForHash().put(fieldKey, "parentQuestionNumber", parentQuestionNumber);
        redisTemplate.opsForHash().put(fieldKey, "tailQuestionNumber", tailQuestionNumber);
        redisTemplate.opsForHash().put(fieldKey, "questionNumber", questionNumber);
        redisTemplate.opsForHash().put(fieldKey, "totalQuestionNumber", totalQuestionNumber);
    }
}