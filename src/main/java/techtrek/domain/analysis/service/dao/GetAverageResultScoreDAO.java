package techtrek.domain.analysis.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAverageResultScoreDAO {
    private final AnalysisRepository analysisRepository;

    // 일치율 평균 ( 해당 달)
    public Double exec(List<String> sessionIds, LocalDateTime startMonth, LocalDateTime endMonth){
        Double MonthAvg = analysisRepository.findAverageScoreBySessionIdsAndDateRange(
                sessionIds, startMonth, endMonth);
        return MonthAvg;
    }
}
