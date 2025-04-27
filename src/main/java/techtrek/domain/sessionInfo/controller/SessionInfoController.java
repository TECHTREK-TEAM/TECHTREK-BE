package techtrek.domain.sessionInfo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techtrek.domain.sessionInfo.dto.SessionInfoRequest;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.service.SessionInfoService;
import techtrek.global.code.ApiResponse;
import techtrek.global.code.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview")
public class SessionInfoController {

    private final SessionInfoService sessionInfoService;

    // 면접 시작하기
    @PostMapping("/start")
    public ResponseEntity<CommonResponse<SessionInfoResponse.Start>> startInterview(@RequestBody SessionInfoRequest.Start request) {
        SessionInfoResponse.Start response = sessionInfoService.startInterview(request.getEnterpriseName(), request.getEnterpriseType());

        return ApiResponse.onSuccess(response);
    }
}
