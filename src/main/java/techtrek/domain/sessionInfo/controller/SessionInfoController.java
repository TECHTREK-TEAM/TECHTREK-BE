package techtrek.domain.sessionInfo.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techtrek.domain.sessionInfo.dto.SessionInfoRequest;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.service.SessionInfoService;
import techtrek.global.common.response.ApiResponse;
import techtrek.global.common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview")
public class SessionInfoController {

    private final SessionInfoService sessionInfoService;

    // 면접 시작하기
    @PostMapping("/start")
    public ResponseEntity<CommonResponse<SessionInfoResponse.Start>> createInterview(@Valid @RequestBody SessionInfoRequest.Start request) {
        SessionInfoResponse.Start response = sessionInfoService.createInterview(request.getEnterpriseName(), request.getEnterpriseType());

        return ApiResponse.onSuccess(response);
    }

    // 새로운 질문 생성하기
    @PostMapping("/questions/new")
    public ResponseEntity<CommonResponse<SessionInfoResponse.NewQuestion>> createNewInterview(@Valid @RequestBody SessionInfoRequest.NewQuestion request) {
        SessionInfoResponse.NewQuestion response = sessionInfoService.createNewInterview(request.getSessionId());

        return ApiResponse.onSuccess(response);
    }

    // 꼬리질문 불러오기
    @PostMapping("/questions/tail")
    public ResponseEntity<CommonResponse<SessionInfoResponse.NewQuestion>> createTailInterview(@Valid @RequestBody SessionInfoRequest.TailQuestion request) {
        SessionInfoResponse.NewQuestion response = sessionInfoService.createTailInterview(request.getSessionId(), request.getParentId());

        return ApiResponse.onSuccess(response);
    }

    // 답변하기
    @PostMapping("/answers")
    public ResponseEntity<CommonResponse<Boolean>> createAnswer(@RequestBody SessionInfoRequest.Answer request) {
        sessionInfoService.createAnswer(request.getSessionId(),request.getFieldId(),request.getType(),request.getAnswer());

        return ApiResponse.onSuccess(true);
    }
}