package techtrek.domain.sessionInfo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "면접 세션 API", description = "면접 생성 및 질문·답변 관련 API")
public class SessionInfoController {
    private final SessionInfoService sessionInfoService;

    // 면접 시작하기
    @PostMapping("/start")
    @Operation( summary = "면접 시작", description = "면접을 시작하고 세션 정보를 생성합니다.")
    public ResponseEntity<CommonResponse<SessionInfoResponse.Start>> createInterview(@RequestBody SessionInfoRequest.Start request) {
        return ApiResponse.onSuccess(sessionInfoService.createInterview(request));
    }

    // 새로운 질문 생성하기
    @PostMapping("/questions/new")
    @Operation( summary = "새로운 질문 생성", description = "면접 도중 새로운 질문을 생성합니다.")
    public ResponseEntity<CommonResponse<SessionInfoResponse.NewQuestion>> createNewInterview(@Valid @RequestBody SessionInfoRequest.NewQuestion request) {
        return ApiResponse.onSuccess(sessionInfoService.createNewInterview(request));
    }

    // 꼬리질문 생성하기
    @PostMapping("/questions/tail")
    @Operation(summary = "꼬리질문 생성", description = "이전 질문의 답변을 기반으로 꼬리질문을 생성합니다.")
    public ResponseEntity<CommonResponse<SessionInfoResponse.TailQuestion>> createTailInterview(@Valid @RequestBody SessionInfoRequest.TailQuestion request) {
        return ApiResponse.onSuccess(sessionInfoService.createTailInterview(request));
    }

    // 답변하기
    @PostMapping("/answers")
    @Operation(summary = "답변 등록", description = "질문에 대한 사용자의 답변을 등록합니다.")
    public ResponseEntity<CommonResponse<Boolean>> createAnswer(@Valid @RequestBody SessionInfoRequest.Answer request) {
        return ApiResponse.onSuccess(sessionInfoService.createAnswer(request));
    }

}