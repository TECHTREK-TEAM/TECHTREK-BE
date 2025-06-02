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
    public RedisRequest.NewQuestion exec(String sessionKey, String fieldId, String basicQuestion, String count, String phase) {
        // 새로운 질문 번호 계산 (기본질문 + 이력서 질문)
        Long newCount = redisTemplate.opsForList().size(sessionKey + ":new");
        String questionNumber = String.valueOf(newCount + 1);

        // 전체 질문 개수 계산 (새로운 질문 + 꼬리질문)
        Long tailCount = redisTemplate.opsForList().size(sessionKey + ":tail");
        Long currentTotalCount = newCount + tailCount;
        String totalQuestionCount = String.valueOf(currentTotalCount + 1);

        // 직접 DTO 생성 후 값 세팅
        RedisRequest.NewQuestion dto = new RedisRequest.NewQuestion();
        dto.setFieldId(fieldId);
        dto.setQuestion(basicQuestion);
        dto.setQuestionNumber(questionNumber);
        dto.setCount(count);
        dto.setPhase(phase);
        dto.setTotalQuestionCount(totalQuestionCount);

        return dto;

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
