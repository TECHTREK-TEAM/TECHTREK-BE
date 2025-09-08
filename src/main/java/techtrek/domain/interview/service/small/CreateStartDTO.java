package techtrek.domain.interview.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.interview.dto.SessionInfoResponse;

@Component
@RequiredArgsConstructor
public class CreateStartDTO {

    // 면접 시작 DTO
    public SessionInfoResponse.Start exec(String sessionId, String fieldId, String question, String questionNumber, String totalQuestionNumber, String sessionInfoId) {

        return SessionInfoResponse.Start.builder()
                .sessionId(sessionId)
                .fieldId(fieldId)
                .question(question)
                .questionNumber(questionNumber)
                .totalQuestionNumber(totalQuestionNumber)
                .sessionInfoId(sessionInfoId)
                .build();

    }

}
