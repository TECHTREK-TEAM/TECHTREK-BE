package techtrek.domain.analysis.dto;

import lombok.*;

// gpt 분석 결과를 파싱해 담는 dto
public class AnalysisParserResponse {

    // 최소 유사도 반환
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LowestSimilarity {
        private String question;
        private String answer;
        private double similarity;
        private String questionNumber;
    }

    // 피드백 GPT 반환
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class feedbackResult {
        private String keyword;
        private String feedback;
    }

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

