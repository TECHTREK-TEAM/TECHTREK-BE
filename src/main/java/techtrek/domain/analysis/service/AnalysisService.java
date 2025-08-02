package techtrek.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.analysis.dto.AnalysisRequest;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.bean.*;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final CreateAnalysisBean createAnalysisBean;
    private final GetAnalysisRecentBean getAnalysisRecentBean;
    private final GetAnalysisListBean getAnalysisListBean;
    private final GetAnalysisBean getAnalysisBean;
    private final DeleteAnalysisBean deleteAnalysisBean;

    // 분석하기
    public AnalysisResponse.Analysis createAnalysis(AnalysisRequest.Analysis request) {
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

    // 선택한 세션 불러오기
    public AnalysisResponse.Detail getAnalysis(String sessionInfoId){
        return getAnalysisBean.exec(sessionInfoId);
    }

    // 선택한 세션 삭제하기
    public Boolean deleteAnalysis(String sessionInfoId){
        return deleteAnalysisBean.exec(sessionInfoId);
    }
}
