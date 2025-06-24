package techtrek.domain.sessionInfo.service.small;

import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;

@Component
public class CreateNewDTO {

    // 새로운 질문 DTO
    public SessionInfoResponse.NewQuestion exec(String fieldId, String question, String questionNumber, String totalQuestionNumber) {

        return SessionInfoResponse.NewQuestion.builder()
                .fieldId(fieldId)
                .question(question)
                .questionNumber(questionNumber)
                .totalQuestionNumber(totalQuestionNumber)
                .build();

    }
}
