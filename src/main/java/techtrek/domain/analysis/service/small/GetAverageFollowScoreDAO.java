package techtrek.domain.analysis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;

@Component
@RequiredArgsConstructor
public class GetAverageFollowScoreDAO {
    private final AnalysisRepository analysisRepository;

    public double exec(){
        return analysisRepository.getAverageFollowScore();

    }
}