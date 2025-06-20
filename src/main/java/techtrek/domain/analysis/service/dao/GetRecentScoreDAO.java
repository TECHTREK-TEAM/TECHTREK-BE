package techtrek.domain.analysis.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetRecentScoreDAO {
    private final AnalysisRepository analysisRepository;

    // 해당 sessionId 중 analysis 가장 높은 점수 하나
    public Analysis exec(List<String> sessionIds){
        Analysis recentAnalysis = analysisRepository.findTopBySessionInfoIdInOrderByCreatedAtDesc(sessionIds)
                .orElse(null);

        return recentAnalysis;
    }
}
