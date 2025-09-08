package techtrek.domain.interview.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BasicQuestionResponse {
    private String question;
    private String answer;
}
