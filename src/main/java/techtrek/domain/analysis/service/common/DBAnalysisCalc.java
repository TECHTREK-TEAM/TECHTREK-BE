package techtrek.domain.analysis.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisParserResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.enterprise.entity.Enterprise;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DBAnalysisCalc {
    private final AnalysisRepository analysisRepository;

    // DB 분석 계산
    public AnalysisParserResponse.DBAnalysisResult exec(Enterprise enterprise,Analysis selectAnalysis){
        // 특정 기업에 속한 모든 분석 결과 조회, 데이터 총 개수
        List<Analysis> allEnterpriseAnalyses = analysisRepository.findAllByEnterprise(enterprise);
        long totalCount = analysisRepository.countByEnterprise(enterprise);

        // 상위 퍼센트 계산 (전체 가준)
        allEnterpriseAnalyses.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
        int rank = 1;
        for (Analysis a : allEnterpriseAnalyses) {
            if (a.getId().equals(selectAnalysis.getId())) { // Long 안전 비교
                break;
            }
            rank++;
        }
        double topPercent = ((double) rank / totalCount) * 100;
        topPercent = Math.round(topPercent * 10) / 10.0;;

        // 소요시간 평균 계산 (user 기준)
        double avgDuration = allEnterpriseAnalyses.stream()
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
                .topScore(topPercent)
                .keyword(selectAnalysis.getKeyword())
                .keywordNumber(selectAnalysis.getKeywordNumber())
                .feedback(selectAnalysis.getFeedback())
                .build();

    }
}
