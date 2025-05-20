package techtrek.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResumeResponse {
        private String sessionId;
        private String fieldId;
        private String question;
        private String questionNumber;
}
