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

        @NotBlank(message = "기업 이름은 필수입니다.")
        private String enterpriseType;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class NewQuestion {
        private String sessionId;
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
    public static class TailQuestion {
        private String sessionId;
        private String parentId;
    }

}
