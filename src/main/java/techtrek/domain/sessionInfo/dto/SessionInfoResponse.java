package techtrek.domain.sessionInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SessionInfoResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Start {
        private String sessionId;
        private String fieldId;
        private String question;
        private String questionNumber;
        private String totalQuestionNumber;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NewQuestion {
        private String fieldId;
        private String question;
        private String questionNumber;
        private String totalQuestionNumber;
    }
}
