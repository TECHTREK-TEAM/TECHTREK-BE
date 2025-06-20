package techtrek.domain.sessionInfo.dto;

import lombok.*;

public class SessionInfoResponse {

    // 면접 시작
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Start {
        private String sessionId;
        private String fieldId;
        private String question;
        private String questionNumber;
        private String totalQuestionNumber;
    }

    // 새 질문
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NewQuestion {
        private String fieldId;
        private String question;
        private String questionNumber;
        private String totalQuestionNumber;
    }

    // 꼬리 질문
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TailQuestion {
        private String fieldId;
        private String question;
        private String parentQuestionNumber;
        private String tailQuestionNumber;
        private String resultTotalQuestionNumber;
    }


}
