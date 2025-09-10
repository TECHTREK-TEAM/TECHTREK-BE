package techtrek.domain.Interview.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.dto.InterviewResponse;

@Component
@RequiredArgsConstructor
public class CreateTailDTO {

    // 꼬리 질문 DTO
    public InterviewResponse.TailQuestion exec(String fieldId, String question, String parentQuestionNumber, String tailQuestionNumber, String totalQuestionNumber) {

        return InterviewResponse.TailQuestion.builder()
                .fieldId(fieldId)
                .question(question)
                .parentQuestionNumber(parentQuestionNumber)
                .tailQuestionNumber(tailQuestionNumber)
                .totalQuestionNumber(totalQuestionNumber)
                .build();

    }
}