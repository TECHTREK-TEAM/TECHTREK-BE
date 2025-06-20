package techtrek.domain.sessionInfo.service.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;

@Component
@RequiredArgsConstructor
public class CreateStartDTO {

    // 면접 시작 Response DTO
    public SessionInfoResponse.Start exec(String sessionId, String fieldId, String question, String questionNumber, String totalQuestionNumber) {

        return SessionInfoResponse.Start.builder()
                .sessionId(sessionId)
                .fieldId(fieldId)
                .question(question)
                .questionNumber(questionNumber)
                .totalQuestionNumber(totalQuestionNumber)
                .build();

    }

}
