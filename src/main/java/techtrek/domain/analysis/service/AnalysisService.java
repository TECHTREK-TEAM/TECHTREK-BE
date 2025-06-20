package techtrek.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.analysis.dto.AnalysisRequest;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.bean.GetAnalysisListBean;
import techtrek.domain.analysis.service.bean.GetAnalysisRecentBean;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.analysis.service.bean.CreateAnalysisBean;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final CreateAnalysisBean createAnalysisBean;
    private final GetAnalysisRecentBean getAnalysisRecentBean;
    private final GetAnalysisListBean getAnalysisListBean;

    //분석하기
    public SessionInfoResponse.Analysis createAnalysis(AnalysisRequest.Analysis request) {
        return createAnalysisBean.exec(request.getSessionId(), request.getDuration());
    }

    // 현재 세션 불러오기
    public AnalysisResponse.Detail getAnalysisRecent(EnterpriseName enterpriseName){
        return getAnalysisRecentBean.exec(enterpriseName);
    }

    // 세션 리스트 불러오기
    public AnalysisResponse.SessionList getAnalysisList(EnterpriseName enterpriseName){
        return getAnalysisListBean.exec(enterpriseName);
    }
}
