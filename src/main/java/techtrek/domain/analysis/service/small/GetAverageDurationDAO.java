package techtrek.domain.analysis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.interviewQuestion.entity.status.EnterpriseName;

@Component
@RequiredArgsConstructor
public class GetAverageDurationDAO {
    private final AnalysisRepository analysisRepository;

    // 평균 소요시간 조회
    public double exec(String userId, EnterpriseName enterpriseName){
        return analysisRepository.getAverageDurationByUserAndEnterprise(userId, enterpriseName);
    }
}
