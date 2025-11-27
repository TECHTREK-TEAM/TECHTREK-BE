package techtrek.domain.session.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import techtrek.domain.session.dto.SessionRequest;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.service.SessionService;
import techtrek.global.common.response.ApiResponse;
import techtrek.global.common.response.CommonResponse;
import techtrek.global.securty.service.CustomUserDetails;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview")
@Tag(name = "면접 세션 API", description = "면접 생성 및 질문·답변 관련 API")
public class SessionController {
    private final SessionService sessionService;

    // 면접 시작하기
    @PostMapping("/start")
    @Operation( summary = "면접 시작", description = "면접을 시작하고 세션 정보를 생성합니다.")
    public ResponseEntity<CommonResponse<SessionResponse.Start>> createInterview(@RequestBody SessionRequest.StartRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(sessionService.createInterview(request, userDetails));
    }

    // 기본 질문 생성하기
    @PostMapping("/questions/basic")
    @Operation( summary = "기본 질문 생성", description = "면접 도중 기본 질문을 생성합니다.")
    public ResponseEntity<CommonResponse<SessionResponse.Question>> createNewInterview(@Valid @RequestBody SessionRequest.QuestionRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(sessionService.createNewInterview(request, userDetails));
    }

    // 이력서 질문 생성하기
    @PostMapping("/questions/resume")
    @Operation( summary = "이력서 질문 생성", description = "면접 도중 이력서 질문을 생성합니다.")
    public ResponseEntity<CommonResponse<SessionResponse.Question>> createResumeInterview(@Valid @RequestBody SessionRequest.QuestionRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(sessionService.createResumeInterview(request, userDetails));
    }

    // 꼬리질문 생성하기
    @PostMapping("/questions/tail")
    @Operation(summary = "꼬리질문 생성", description = "이전 질문의 답변을 기반으로 꼬리질문을 생성합니다.")
    public ResponseEntity<CommonResponse<SessionResponse.TailQuestion>> createTailInterview(@Valid @RequestBody SessionRequest.TailQuestionRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(sessionService.createTailInterview(request, userDetails));
    }

    // 답변하기
    @PostMapping("/answers")
    @Operation(summary = "답변 등록", description = "질문에 대한 사용자의 답변을 등록합니다.")
    public ResponseEntity<CommonResponse<Boolean>> createAnswer(@Valid @RequestBody SessionRequest.AnswerRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ApiResponse.onSuccess(sessionService.createAnswer(request, userDetails));
    }

    // 종료하기
    @DeleteMapping("/close/{sessionId}")
    @Operation(summary = "면접 종료", description = "면접을 종료합니다 (분석 x, 데이터 삭제)")
    public ResponseEntity<CommonResponse<Boolean>> deleteInterview(@Parameter(description = "면접 ID", required = true) @PathVariable String sessionId, @AuthenticationPrincipal CustomUserDetails userDetails){
        return ApiResponse.onSuccess(sessionService.deleteInterview(sessionId, userDetails));
    }

}