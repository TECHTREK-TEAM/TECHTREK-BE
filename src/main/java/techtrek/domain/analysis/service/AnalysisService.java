package techtrek.domain.analysis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.analysis.dto.AnalysisRequest;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.component.*;
import techtrek.global.securty.service.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class AnalysisService {
    private final CreateAnalysis createAnalysis;
    private final GetAnalysisRecent getAnalysisRecent;
    private final GetAnalysisList getAnalysisList;
    private final GetAnalysis getAnalysis;
    private final DeleteAnalysis deleteAnalysis;

    // 분석하기
    public AnalysisResponse.Analysis createAnalysis(AnalysisRequest.Analysis request, CustomUserDetails userDetails) {
        return createAnalysis.exec(request.getSessionId(), request.getDuration(), userDetails);
    }

    // 현재 세션 불러오기
    public AnalysisResponse.Detail getAnalysisRecent(String enterpriseName, CustomUserDetails userDetails){
        return getAnalysisRecent.exec(enterpriseName, userDetails);
    }

    // 세션 리스트 불러오기
    public AnalysisResponse.AnalysisList getAnalysisList(String enterpriseName, CustomUserDetails userDetails){
        return getAnalysisList.exec(enterpriseName, userDetails);
    }

    // 선택한 세션 불러오기
    public AnalysisResponse.Detail getAnalysis(Long analysisId, CustomUserDetails userDetails){
        return getAnalysis.exec(analysisId,userDetails);
    }

    // 선택한 세션 삭제하기
    public Boolean deleteAnalysis(Long analysisId, CustomUserDetails userDetails){
        return deleteAnalysis.exec(analysisId,userDetails);
    }
}
