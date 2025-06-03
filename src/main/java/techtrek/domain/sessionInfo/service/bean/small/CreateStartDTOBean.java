package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;

@Component
@RequiredArgsConstructor
public class CreateStartDTOBean {

    // 면접 시작 Response DTO
    public SessionInfoResponse.Start exec(String sessionId, String fieldId, String question, String questionNumber, String totalQuestionNumber) {

        SessionInfoResponse.Start response = new SessionInfoResponse.Start();
        response.setSessionId(sessionId);
        response.setFieldId(fieldId);
        response.setQuestion(question);
        response.setQuestionNumber(questionNumber);
        response.setTotalQuestionNumber(totalQuestionNumber);

        return response;

    }

}
