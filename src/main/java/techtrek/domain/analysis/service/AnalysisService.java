package techtrek.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.analysis.dto.AnalysisRequest;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.bean.GetAnalysisListBean;
import techtrek.domain.analysis.service.bean.GetAnalysisRecentBean;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final GetAnalysisRecentBean getAnalysisRecentBean;
    private final GetAnalysisListBean getAnalysisListBean;

    // 현재 세션 불러오기
    public AnalysisResponse.Detail getAnalysisRecent(AnalysisRequest.Recent request){
        return getAnalysisRecentBean.exec(request.getEnterpriseName());
    }

    // 세션 리스트 불러오기
    public AnalysisResponse.SessionList getAnalysisList(AnalysisRequest.Recent request){
        return getAnalysisListBean.exec(request.getEnterpriseName());
    }
}
