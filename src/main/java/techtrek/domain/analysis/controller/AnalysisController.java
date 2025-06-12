package techtrek.domain.analysis.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techtrek.domain.analysis.dto.AnalysisRequest;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.AnalysisService;
import techtrek.global.common.response.ApiResponse;
import techtrek.global.common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final AnalysisService analysisService;

    // 세션 불러오기
    @PostMapping("/recent")
    public ResponseEntity<CommonResponse<AnalysisResponse.Detail>> getAnalysisRecent(@RequestBody AnalysisRequest.Recent request){
        return ApiResponse.onSuccess(analysisService.getAnalysisRecent(request));
    }
}
