package techtrek.domain.analysis.dto;

import lombok.*;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;

public class AnalysisRequest {

    // 세션 불러오기
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Recent{
        public EnterpriseName enterpriseName;
    }
}
