package techtrek.domain.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class InterviewResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Start {
        private String sessionId;
        private String fieldId;
        private String question;
    }
}
