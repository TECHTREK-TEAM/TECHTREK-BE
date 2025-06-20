package techtrek.domain.analysis.dto;

import lombok.*;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;

import java.time.LocalDateTime;
import java.util.List;

public class AnalysisResponse {

    // 분석
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Analysis {
        private String analysisId;
        private Boolean status;
        private Double resultScore;
        private Double followScore;
        private String result;
        private int duration;
        private String keyword;
    }


    // 세션 불러오기
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Detail{
        private String sessionInfoId;
        private Analysis analysis;
        private List<Interview> interview;
        private Feedback feedback;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Analysis {
            private boolean status;
            private double followScore;
            private double averageFollowPercent;
            private double resultScore;
            private int duration;
            private double averageDurationPercent;
            private double topScore;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Interview {
            private String question;
            private String answer;
            private String questionNumber;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Feedback {
            private String keyword;
            private String keywordNumber;
            private String result;
        }
    }

    // 세션 리스트 불러오기
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SessionList {
        private List<SessionData> session;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SessionData {
        private String sessionInfoId;
        private EnterpriseName enterpriseName;
        private LocalDateTime createdAt;
    }
}
