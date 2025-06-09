package techtrek.domain.sessionInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.NotBlank;

public class SessionInfoRequest {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Start {
        @NotBlank(message = "기업 이름은 필수입니다.")
        private String enterpriseName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class NewQuestion {
        @NotBlank(message = "세션Id는 필수입니다.")
        private String sessionId;

        @NotBlank(message = "이전 필드Id는 필수입니다.")
        private String previousId;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class TailQuestion {
        @NotBlank(message = "세션Id는 필수입니다.")
        private String sessionId;

        @NotBlank(message = "부모Id는 필수입니다.")
        private String parentId;
        private String previousId;
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class Answer {
        private String sessionId;
        private String fieldId;
        private String type;
        private String answer;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Analysis {
        private String sessionId;
        private int duration;
    }

}
