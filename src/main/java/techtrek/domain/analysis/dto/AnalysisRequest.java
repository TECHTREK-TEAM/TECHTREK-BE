package techtrek.domain.analysis.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AnalysisRequest {

    // 분석하기
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Analysis {
        @NotBlank(message = "세션Id는 필수입니다.")
        private String sessionId;

        private int duration;
    }
}
