package techtrek.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.analysis.dto.AnalysisRequest;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.bean.GetAnalysisRecent;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final GetAnalysisRecent getAnalysisRecent;

    // 세션 불러오기
    public AnalysisResponse.Detail getAnalysisRecent(AnalysisRequest.Recent request){
        return getAnalysisRecent.exec(request.getEnterpriseName());
    }
}
