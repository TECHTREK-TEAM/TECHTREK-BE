package techtrek.domain.analysis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;

@Component
@RequiredArgsConstructor
public class GetAvgDurationDAO {
    private final AnalysisRepository analysisRepository;

    // 평균 소요시간 조회
    public double exec(){
       return analysisRepository.getAverageDuration();

    }
}
