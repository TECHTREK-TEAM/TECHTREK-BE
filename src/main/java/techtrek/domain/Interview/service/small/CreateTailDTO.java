package techtrek.domain.Interview.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.SessionResponse;

@Component
@RequiredArgsConstructor
public class CreateTailDTO {

    // 꼬리 질문 DTO
    public SessionResponse.TailQuestion exec(String fieldId, String question, String parentQuestionNumber, String tailQuestionNumber, String totalQuestionNumber) {

        return SessionResponse.TailQuestion.builder()
                .fieldId(fieldId)
                .question(question)
                .parentQuestionNumber(parentQuestionNumber)
                .tailQuestionNumber(tailQuestionNumber)
                .totalQuestionNumber(totalQuestionNumber)
                .build();

    }
}