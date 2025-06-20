package techtrek.domain.analysis.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTotalAverageResultScoreDAO {
    private final AnalysisRepository analysisRepository;

    // 일치율 평균 (총)
    public Double exec(List<String> sessionIds){
        Double average = analysisRepository.findAverageScoreBySessionIds(sessionIds);
        return average;
    }
}
