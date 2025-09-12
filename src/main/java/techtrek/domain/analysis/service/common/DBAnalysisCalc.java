package techtrek.domain.analysis.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisParserResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.user.entity.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DBAnalysisCalc {
    private final AnalysisRepository analysisRepository;

    // DB 분석 계산
    public AnalysisParserResponse.DBAnalysisResult exec(User user, Enterprise enterprise,Analysis selectAnalysis){
        // user 기준 평균
        List<Analysis> allAnalyses = analysisRepository.findAllByUserAndEnterprise(user,enterprise);
        // 전체 사용자 기준 평균
        List<Analysis> allEnterpriseAnalyses = analysisRepository.findAllByEnterprise(enterprise);

        // 상위 퍼센트 계산 (전체 가준)
        allEnterpriseAnalyses.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        int rank = 1;
        for (Analysis a : allEnterpriseAnalyses) {
            if (a.getId().equals(selectAnalysis.getId())) break;
            rank++;
        }
        double topScore = ((double) rank / allEnterpriseAnalyses.size()) * 100;
        topScore = Math.round(topScore * 10) / 10.0;

        // 소요시간 평균 계산 (user 기준)
        double avgDuration = allAnalyses.stream()
                .mapToInt(Analysis::getDuration)
                .average()
                .orElse(0.0);

        double averageDurationPercent = avgDuration == 0 ? 0
                : Math.round(((selectAnalysis.getDuration() - avgDuration) / avgDuration) * 1000) / 10.0;

        return AnalysisParserResponse.DBAnalysisResult.builder()
                .analysisId(selectAnalysis.getId())
                .sessionId(selectAnalysis.getSessionId())
                .isPass(selectAnalysis.isPass())
                .score(selectAnalysis.getScore())
                .duration(selectAnalysis.getDuration())
                .averageDurationPercent(averageDurationPercent)
                .topScore(topScore)
                .keyword(selectAnalysis.getKeyword())
                .keywordNumber(selectAnalysis.getKeywordNumber())
                .feedback(selectAnalysis.getFeedback())
                .build();

    }
}
