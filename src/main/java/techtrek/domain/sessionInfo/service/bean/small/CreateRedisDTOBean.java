package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;

@Component
@RequiredArgsConstructor
public class CreateRedisDTOBean {
    private final RedisTemplate<String, String> redisTemplate;

    // 새로운 질문 RedisDTO
    public RedisRequest.NewQuestion exec(String newKey, String phase, String count, String basicQuestion, String questionNumber,String totalQuestionNumber ) {

        redisTemplate.opsForHash().put(newKey, "phase", phase);
        redisTemplate.opsForHash().put(newKey, "count", count);
        redisTemplate.opsForHash().put(newKey, "question", basicQuestion);
        redisTemplate.opsForHash().put(newKey, "questionNumber", questionNumber);
        redisTemplate.opsForHash().put(newKey, "totalQuestionNumber", totalQuestionNumber);

        // DTO 생성 및 반환
        RedisRequest.NewQuestion dto = new RedisRequest.NewQuestion();
        dto.setFieldId("1");
        dto.setQuestion(basicQuestion);
        dto.setQuestionNumber(questionNumber);
        dto.setCount(count);
        dto.setPhase(phase);
        dto.setTotalQuestionCount(totalQuestionNumber); // 현재 총 질문 개수 + 1
        return  dto;


    }

    // 꼬리질문 Map 생성
    public RedisRequest.TailQuestion exec(String fieldId, String question, String questionNumber, String totalQuestionCount) {
        RedisRequest.TailQuestion dto = new RedisRequest.TailQuestion();
        dto.setFieldId(fieldId);
        dto.setQuestion(question);
        dto.setQuestionNumber(questionNumber);
        dto.setTotalQuestionCount(totalQuestionCount);
        return dto;
    }
}
