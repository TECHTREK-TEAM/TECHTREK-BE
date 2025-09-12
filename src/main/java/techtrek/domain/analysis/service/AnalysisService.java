package techtrek.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.analysis.dto.AnalysisRequest;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.component.*;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final CreateAnalysis createAnalysis;
    private final GetAnalysisRecent getAnalysisRecent;
    private final GetAnalysisList getAnalysisList;
    private final GetAnalysis getAnalysis;
//    private final DeleteAnalysisBean deleteAnalysisBean;

    // 분석하기
    public AnalysisResponse.Analysis createAnalysis(AnalysisRequest.Analysis request) {
        return createAnalysis.exec(request.getSessionId(), request.getDuration());
    }

    // 현재 세션 불러오기
    public AnalysisResponse.Detail getAnalysisRecent(String enterpriseName){
        return getAnalysisRecent.exec(enterpriseName);
    }

    // 세션 리스트 불러오기
    public AnalysisResponse.AnalysisList getAnalysisList(String enterpriseName){
        return getAnalysisList.exec(enterpriseName);
    }

    // 선택한 세션 불러오기
    public AnalysisResponse.Detail getAnalysis(String analysisId){
        return getAnalysis.exec(analysisId);
    }
//
//    // 선택한 세션 삭제하기
//    public Boolean deleteAnalysis(String sessionInfoId){
//        return deleteAnalysisBean.exec(sessionInfoId);
//    }
}
