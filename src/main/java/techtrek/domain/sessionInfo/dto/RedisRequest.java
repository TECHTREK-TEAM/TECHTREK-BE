package techtrek.domain.sessionInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RedisRequest {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NewQuestion {
        private String fieldId;
        private String question;
        private String answer = ""; // default
        private String questionNumber;
        private String count;
        private String phase;
        private String totalQuestionCount;
    }


}
