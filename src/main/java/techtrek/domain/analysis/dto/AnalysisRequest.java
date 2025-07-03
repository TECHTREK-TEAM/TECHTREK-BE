package techtrek.domain.analysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AnalysisRequest {

    // 분석하기
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "분석 데이터 요청")
    public static class Analysis {
        @Schema(description = "세션 ID", example = "1234", required = true)
        @NotBlank(message = "세션Id는 필수입니다.")
        private String sessionId;

        @Schema(description = "분석 소요 시간(분)", example = "5", required = true)
        private int duration;
    }
}
