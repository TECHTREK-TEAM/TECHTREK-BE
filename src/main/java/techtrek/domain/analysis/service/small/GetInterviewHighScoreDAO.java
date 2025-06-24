package techtrek.domain.analysis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetInterviewHighScoreDAO {
    private final AnalysisRepository analysisRepository;

    // 해당 sessionId 중 analysis 점수가 가장 높은 데이터 조회
    public Analysis exec(List<String> sessionIds){
        return analysisRepository.findTopBySessionInfoIdInOrderByResultScoreDesc(sessionIds)
                .orElse(null);
    }
}
