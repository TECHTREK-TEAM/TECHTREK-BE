package techtrek.domain.analysis.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techtrek.domain.analysis.dto.AnalysisRequest;
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

    // 분석하기
    @PostMapping
    public ResponseEntity<CommonResponse<AnalysisResponse.Analysis>> createAnalysis(@Valid @RequestBody AnalysisRequest.Analysis request) {
        return ApiResponse.onSuccess(analysisService.createAnalysis(request));
    }

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

    // 선택한 세션 불러오기
    @GetMapping("/{sessionInfoId}")
    public ResponseEntity<CommonResponse<AnalysisResponse.Detail>> getAnalysis(@PathVariable String sessionInfoId){
        return ApiResponse.onSuccess(analysisService.getAnalysis(sessionInfoId));
    }

    // 선택한 세션 삭제
    @DeleteMapping("/{sessionInfoId}")
    public ResponseEntity<CommonResponse<Boolean>> deleteAnalysis(@PathVariable String sessionInfoId){
        return ApiResponse.onSuccess(analysisService.deleteAnalysis(sessionInfoId));
    }

}
