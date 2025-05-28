package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;

@Component
@RequiredArgsConstructor
public class CreateRedisNewDTOBean {

    // 새로운 질문 Map 생성
    public RedisRequest.NewQuestion exec(String fieldId, String question, String questionNumber, String count, String phase, String totalQuestionCount) {
        RedisRequest.NewQuestion dto = new RedisRequest.NewQuestion();
        dto.setFieldId(fieldId);
        dto.setQuestion(question);
        dto.setQuestionNumber(questionNumber);
        dto.setCount(count);
        dto.setPhase(phase);
        dto.setTotalQuestionCount(totalQuestionCount);
        return dto;
    }
}
