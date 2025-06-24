package techtrek.domain.analysis.service.small;

import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;

@Component
public class CreateAnalysisDTO {

    // 분석하기 결과 DTO
    public AnalysisResponse.Analysis exec(Analysis analysis){
        return AnalysisResponse.Analysis.builder()
                .analysisId(analysis.getId())
                .status(analysis.isStatus())
                .resultScore(analysis.getResultScore())
                .followScore(analysis.getFollowScore())
                .result(analysis.getResult())
                .duration(analysis.getDuration())
                .keyword(analysis.getKeyword())
                .build();

    }
}
