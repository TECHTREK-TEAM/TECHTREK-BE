package techtrek.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.analysis.dto.AnalysisRequest;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.bean.GetAnalysisList;
import techtrek.domain.analysis.service.bean.GetAnalysisRecent;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final GetAnalysisRecent getAnalysisRecent;
    private final GetAnalysisList getAnalysisList;

    // 세션 불러오기
    public AnalysisResponse.Detail getAnalysisRecent(AnalysisRequest.Recent request){
        return getAnalysisRecent.exec(request.getEnterpriseName());
    }

    // 세션 리스트 불러오기
    public AnalysisResponse.SessionList getAnalysisList(AnalysisRequest.Recent request){
        return getAnalysisList.exec(request.getEnterpriseName());
    }
}
