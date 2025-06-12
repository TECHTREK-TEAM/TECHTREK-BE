package techtrek.domain.analysis.dto;

import lombok.*;
import java.util.List;

public class AnalysisResponse {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
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
}
