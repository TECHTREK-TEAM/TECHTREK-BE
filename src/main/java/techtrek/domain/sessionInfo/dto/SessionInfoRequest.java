package techtrek.domain.sessionInfo.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;

public class SessionInfoRequest {

    // 면접 시작
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Start {
        private EnterpriseName enterpriseName;
    }

    // 새 질문
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NewQuestion {
        @NotBlank(message = "세션Id는 필수입니다.")
        private String sessionId;

        @NotBlank(message = "이전 필드Id는 필수입니다.")
        private String previousId;
    }

    // 꼬리 질문
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TailQuestion {
        @NotBlank(message = "세션Id는 필수입니다.")
        private String sessionId;

        @NotBlank(message = "부모Id는 필수입니다.")
        private String parentId;
        private String previousId;
    }

    // 답변
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Answer {
        @NotBlank(message = "세션Id 필수입니다.")
        private String sessionId;

        @NotBlank(message = "필드Id는 필수입니다.")
        private String fieldId;

        @NotBlank(message = "타입(new/tail)은 필수입니다.")
        private String type;

        @NotBlank(message = "답변은 필수입니다.")
        private String answer;
    }

}
