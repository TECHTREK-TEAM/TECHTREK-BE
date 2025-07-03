package techtrek.domain.analysis.dto;

import lombok.*;

// gpt 분석 결과를 파싱해 담는 dto
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalysisParserResponse {

    private Evaluation evaluation;
    private KeyKeywords keyKeywords;
    private double totalScore;
    private String result;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Evaluation {
        private ScoreDetail accuracy;
        private ScoreDetail keyConcept;
        private ScoreDetail companyFit;
        private ScoreDetail logic;
        private ScoreDetail followScore;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScoreDetail {
        private double score;
        private String reason;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class KeyKeywords {
        private String keyword;
        private String questionNumber;
    }
}

