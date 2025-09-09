package techtrek.domain.session.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;
import jakarta.validation.constraints.NotBlank;

public class SessionRequest {

    // 면접 시작
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "면접 시작 요청")
    public static class Start {
        @Schema(description = "기업 이름", example = "네이버")
        private String enterpriseName;
    }

    // 새 질문
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "새 질문 생성 요청")
    public static class Question {
        @Schema(description = "세션 ID", example = "1234", required = true)
        @NotBlank(message = "세션Id는 필수입니다.")
        private String sessionId;
    }

    // 꼬리 질문
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "꼬리 질문 생성 요청")
    public static class TailQuestion {
        @Schema(description = "세션 ID", example = "1234", required = true)
        @NotBlank(message = "세션Id는 필수입니다.")
        private String sessionId;

        @Schema(description = "부모 질문 ID", example = "2345", required = true)
        @NotBlank(message = "부모Id는 필수입니다.")
        private String parentId;

        @Schema(description = "이전 필드 ID", example = "5678")
        private String previousId;
    }

    // 답변
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "답변 생성 요청")
    public static class Answer {
        @Schema(description = "세션 ID", example = "1234", required = true)
        @NotBlank(message = "세션Id 필수입니다.")
        private String sessionId;

        @Schema(description = "필드 ID", example = "2345", required = true)
        @NotBlank(message = "필드Id는 필수입니다.")
        private String fieldId;

        @Schema(description = "타입 (new/tail)", example = "new", required = true)
        @NotBlank(message = "타입(new/tail)은 필수입니다.")
        private String type;

        @Schema(description = "답변 내용", example = "잘 모르겠습니다.", required = true)
        @NotBlank(message = "답변은 필수입니다.")
        @Size(min = 1, max = 500, message = "답변은 1자 이상 500자 이하로 작성해주세요.")
        private String answer;
    }

}
