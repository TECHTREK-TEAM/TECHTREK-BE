package techtrek.domain.sessionInfo.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnalysisParserResponse {

    private Evaluation evaluation;
    private KeyKeywords keyKeywords;
    private double totalScore;
    private String result;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Evaluation {
        private ScoreDetail accuracy;
        private ScoreDetail keyConcept;
        private ScoreDetail companyFit;
        private ScoreDetail logic;
        private ScoreDetail followScore;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ScoreDetail {
        private double score;
        private String reason;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class KeyKeywords {
        private String keyword;
        private String totalQuestionNumber;
    }
}

