package techtrek.domain.analysis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;

@Component
@RequiredArgsConstructor
public class GetEnterpriseAnalysisCountDAO {
    private final AnalysisRepository analysisRepository;

    // 기업명 기준으로 전체 분석 개수 조회
    public long exec(EnterpriseName enterpriseName){
        return analysisRepository.countByEnterprise(enterpriseName);

    }

    // 기업명, 특정점수보다 낮은 분석 데이터 수 조회
    public long exec(EnterpriseName enterpriseName, double resultScore){
        return analysisRepository.countLowerScoreInEnterprise(enterpriseName, resultScore);
    }
}
