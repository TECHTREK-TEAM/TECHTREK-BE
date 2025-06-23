package techtrek.domain.sessionInfo.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;

@Component
@RequiredArgsConstructor
public class CreateTailDTO {

    // 꼬리 질문 Response DTO
    public SessionInfoResponse.TailQuestion exec(String fieldId, String question, String parentQuestionNumber, String tailQuestionNumber, String totalQuestionNumber) {

        return SessionInfoResponse.TailQuestion.builder()
                .fieldId(fieldId)
                .question(question)
                .parentQuestionNumber(parentQuestionNumber)
                .tailQuestionNumber(tailQuestionNumber)
                .totalQuestionNumber(totalQuestionNumber)
                .build();

    }
}