package techtrek.domain.sessionInfo.service.bean.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreatetailCountHelper {
    private final RedisTemplate<String, String> redisTemplate;

    public String exec(String parentQuestionNumber, String tailCountKey){
        // 꼬리질문 번호 생성
        String tailCountStr = redisTemplate.opsForValue().get(tailCountKey);
        int tailCount = tailCountStr == null ? 0 : Integer.parseInt(tailCountStr);
        tailCount++; // 새 꼬리질문 번호 증가
        redisTemplate.opsForValue().set(tailCountKey, String.valueOf(tailCount));

        String newTailCount = parentQuestionNumber + "-" + tailCount;

        return newTailCount;
    }
}
