package techtrek.domain.sessionInfo.service.dto;

import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;

@Component
public class CreateNewDTO {

    // 새로운 질문 Response DTO
    public SessionInfoResponse.NewQuestion exec(String fieldId, String question, String questionNumber, String totalQuestionNumber) {

        SessionInfoResponse.NewQuestion response = new SessionInfoResponse.NewQuestion();
        response.setFieldId(fieldId);
        response.setQuestion(question);
        response.setQuestionNumber(questionNumber);
        response.setTotalQuestionNumber(totalQuestionNumber);

        return response;

    }
}
