package techtrek.domain.analysis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.AnalysisService;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.global.common.response.ApiResponse;
import techtrek.global.common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analyses")
public class AnalysisController {
    private final AnalysisService analysisService;

    // 현재 세션 불러오기
    @GetMapping("/recent/{enterpriseName}")
    public ResponseEntity<CommonResponse<AnalysisResponse.Detail>> getAnalysisRecent(@PathVariable EnterpriseName enterpriseName){
        return ApiResponse.onSuccess(analysisService.getAnalysisRecent(enterpriseName));
    }

    // 세션 리스트 불러오기
    @GetMapping("/sessions/{enterpriseName}")
    public ResponseEntity<CommonResponse<AnalysisResponse.SessionList>> getAnalysisList(@PathVariable EnterpriseName enterpriseName){
        return ApiResponse.onSuccess(analysisService.getAnalysisList(enterpriseName));
    }
}
