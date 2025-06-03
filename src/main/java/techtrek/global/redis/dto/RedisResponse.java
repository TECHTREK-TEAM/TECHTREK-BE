package techtrek.global.redis.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class RedisResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldData {
        private String phase;
        private String count;
        private String question;
        private String answer;
        private String questionNumber;
        private String totalQuestionNumber;

    }
}
