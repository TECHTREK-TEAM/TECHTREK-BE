package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.sessionInfo.entity.SessionInfo;

import java.util.UUID;

@Component
@RequiredArgsConstructor


public class SaveAnalysisDAOBean {
    private final AnalysisRepository analysisRepository;

    // 분석 테이블 저장
    public String exec(SessionInfo sessionInfo, Boolean status, Double resultScore, double followScore, String result, String keyword, String totalQuestionNumber, String userGroup, int duration){
        Analysis analysis = Analysis.builder()
                .id(UUID.randomUUID().toString())
                .status(status)
                .resultScore(resultScore)
                .followScore(followScore)
                .result(result)
                .keyword(keyword)
                .keywordNumber(totalQuestionNumber)
                .analysisGroup(userGroup)
                .duration(duration)
                .sessionInfo(sessionInfo)
                .build();

        analysisRepository.save(analysis);

        return analysis.getId();
    }
}