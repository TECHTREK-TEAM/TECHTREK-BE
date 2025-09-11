package techtrek.domain.analysis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techtrek.domain.analysis.dto.AnalysisRequest;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.AnalysisService;
//import techtrek.domain.interviewQuestion.entity.status.EnterpriseName;
import techtrek.global.common.response.ApiResponse;
import techtrek.global.common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analyses")
@Tag(name = "분석 API", description = "면접 분석 관련 API")
public class AnalysisController {
    private final AnalysisService analysisService;

    // 분석하기
    @PostMapping
    @Operation(summary = "분석 생성", description = "새로운 분석 데이터를 생성합니다.")
    public ResponseEntity<CommonResponse<AnalysisResponse.Analysis>> createAnalysis(@Valid @RequestBody AnalysisRequest.Analysis request) {
        return ApiResponse.onSuccess(analysisService.createAnalysis(request));
    }

    // 현재 세션 불러오기
//    @GetMapping("/recent/{enterpriseName}")
//    @Operation(summary = "최근 분석 조회", description = "해당 기업의 최근 분석 세션을 조회합니다.")
//    public ResponseEntity<CommonResponse<AnalysisResponse.Detail>> getAnalysisRecent(@Parameter(description = "기업 이름", required = true) @PathVariable EnterpriseName enterpriseName){
//        return ApiResponse.onSuccess(analysisService.getAnalysisRecent(enterpriseName));
//    }
//
//    // 세션 리스트 불러오기
//    @GetMapping("/sessions/{enterpriseName}")
//    @Operation(summary = "분석 세션 리스트 조회", description = "해당 기업의 모든 분석 세션 리스트를 조회합니다.")
//    public ResponseEntity<CommonResponse<AnalysisResponse.SessionList>> getAnalysisList(@Parameter(description = "기업 이름", required = true) @PathVariable EnterpriseName enterpriseName){
//        return ApiResponse.onSuccess(analysisService.getAnalysisList(enterpriseName));
//    }
//
//    // 선택한 세션 불러오기
//    @GetMapping("/{sessionInfoId}")
//    @Operation(summary = "분석 세션 상세 조회", description = "선택한 분석 세션의 상세 정보를 조회합니다.")
//    public ResponseEntity<CommonResponse<AnalysisResponse.Detail>> getAnalysis(@Parameter(description = "면접 ID", required = true) @PathVariable String sessionInfoId){
//        return ApiResponse.onSuccess(analysisService.getAnalysis(sessionInfoId));
//    }
//
//    // 선택한 세션 삭제
//    @DeleteMapping("/{sessionInfoId}")
//    @Operation(summary = "분석 세션 삭제", description = "선택한 분석 세션을 삭제합니다.")
//    public ResponseEntity<CommonResponse<Boolean>> deleteAnalysis(@Parameter(description = "면접 ID", required = true) @PathVariable String sessionInfoId){
//        return ApiResponse.onSuccess(analysisService.deleteAnalysis(sessionInfoId));
//    }

}
