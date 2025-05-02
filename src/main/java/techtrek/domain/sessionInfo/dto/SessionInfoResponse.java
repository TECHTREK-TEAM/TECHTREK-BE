package techtrek.domain.sessionInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SessionInfoResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Start {
        private String sessionId;
        private String fieldId;
        private String question;
        private String questionNumber;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class NewQuestion {
        private String fieldId;
        private String question;
        private String questionNumber;
    }
}
