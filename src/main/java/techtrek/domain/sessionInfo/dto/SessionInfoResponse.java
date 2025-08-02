package techtrek.domain.sessionInfo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class SessionInfoResponse {

    // 면접 시작
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "면접 시작 응답")
    public static class Start {
        @Schema(description = "세션 ID", example = "1234")
        private String sessionId;

        @Schema(description = "필드 ID", example = "2345")
        private String fieldId;

        @Schema(description = "질문 내용", example = "자기소개를 해주세요.")
        private String question;

        @Schema(description = "현재 질문 번호", example = "1")
        private String questionNumber;

        @Schema(description = "전체 질문 수", example = "10")
        private String totalQuestionNumber;

        @Schema(description = "면접 ID", example = "10")
        private String sessionInfoId;
    }

    // 새 질문
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "새 질문 생성 응답")
    public static class NewQuestion {
        @Schema(description = "필드 ID", example = "2345")
        private String fieldId;

        @Schema(description = "질문 내용", example = "최근 프로젝트 경험에 대해 말씀해 주세요.")
        private String question;

        @Schema(description = "현재 질문 번호", example = "2")
        private String questionNumber;

        @Schema(description = "전체 질문 수", example = "10")
        private String totalQuestionNumber;
    }

    // 꼬리 질문
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "꼬리 질문 생성 응답")
    public static class TailQuestion {
        @Schema(description = "필드 ID", example = "2345")
        private String fieldId;

        @Schema(description = "질문 내용", example = "그 프로젝트에서 가장 어려웠던 점은 무엇인가요?")
        private String question;

        @Schema(description = "부모 질문 번호", example = "2")
        private String parentQuestionNumber;

        @Schema(description = "꼬리 질문 번호", example = "2-1")
        private String tailQuestionNumber;

        @Schema(description = "전체 질문 수", example = "10")
        private String totalQuestionNumber;
    }


}
