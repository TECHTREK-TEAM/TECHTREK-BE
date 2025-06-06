package techtrek.global.redis.dto;

import lombok.*;

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

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class ListData {
        private String question;
        private String answer;
        private int totalQuestionNumber;
        private String tailQuestionMessage;
    }
}
