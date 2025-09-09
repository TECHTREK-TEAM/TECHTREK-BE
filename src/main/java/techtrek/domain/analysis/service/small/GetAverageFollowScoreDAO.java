package techtrek.domain.analysis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.interviewQuestion.entity.status.EnterpriseName;

@Component
@RequiredArgsConstructor
public class GetAverageFollowScoreDAO {
    private final AnalysisRepository analysisRepository;

    // 연계질문 대응력 평균 조회
    public double exec(String userId, EnterpriseName enterpriseName){
        return analysisRepository.getAverageFollowScoreByUserAndEnterprise(userId, enterpriseName);
    }
}