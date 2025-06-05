package techtrek.domain.sessionInfo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class SessionInfoResponse {

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Start {
        private String sessionId;
        private String fieldId;
        private String question;
        private String questionNumber;
        private String totalQuestionNumber;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class NewQuestion {
        private String fieldId;
        private String question;
        private String questionNumber;
        private String totalQuestionNumber;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TailQuestion {
        private String fieldId;
        private String question;
        private String parentQuestionNumber;
        private String tailQuestionNumber;
        private String resultTotalQuestionNumber;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Analysis {
        private String AnalysisId;
        private Boolean expectation;
        private Double matchRate;
        private int followUpHanding;
        private String result;
        private int duration;
        private String keyword;
    }


}
