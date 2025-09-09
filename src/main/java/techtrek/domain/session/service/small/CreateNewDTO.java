package techtrek.domain.session.service.small;

import org.springframework.stereotype.Component;
import techtrek.domain.session.dto.SessionResponse;

@Component
public class CreateNewDTO {

    // 새로운 질문 DTO
    public SessionResponse.NewQuestion exec(String fieldId, String question, String questionNumber, String totalQuestionNumber) {

        return SessionResponse.NewQuestion.builder()
                .fieldId(fieldId)
                .question(question)
                .questionNumber(questionNumber)
                .totalQuestionNumber(totalQuestionNumber)
                .build();

    }
}
