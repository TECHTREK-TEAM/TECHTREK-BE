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
    public ResponseEntity<CommonResponse<SessionInfoResponse.Start>> createInterview(@RequestBody SessionInfoRequest.Start request) {
        SessionInfoResponse.Start response = sessionInfoService.createInterview(request.getEnterpriseName(), request.getEnterpriseType());

        return ApiResponse.onSuccess(response);
    }

    // 새로운 질문 불러오기
    @GetMapping("/questions/new/{sessionId}")
    public ResponseEntity<CommonResponse<SessionInfoResponse.NewQuestion>> getNewInterview(@PathVariable String sessionId) {
        SessionInfoResponse.NewQuestion response = sessionInfoService.getNewInterview(sessionId);

        return ApiResponse.onSuccess(response);
    }

    // 답변하기
    @PostMapping("/answers")
    public ResponseEntity<CommonResponse<Boolean>> createAnswer(@RequestBody SessionInfoRequest.Answer request) {
        sessionInfoService.createAnswer(request.getSessionId(),request.getFieldId(),request.getType(),request.getAnswer());

        return ApiResponse.onSuccess(true);
    }
}