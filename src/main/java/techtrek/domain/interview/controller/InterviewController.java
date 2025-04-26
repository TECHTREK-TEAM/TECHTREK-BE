package techtrek.domain.interview.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import techtrek.domain.interview.dto.InterviewResponse;
import techtrek.domain.interview.service.InterviewService;
import techtrek.global.code.CommonResponse;
import techtrek.global.code.status.ResponseCode;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interview")
public class InterviewController {

    private final InterviewService interviewService;

    // 면접 시작하기
    @GetMapping("/start")
    public ResponseEntity<CommonResponse<InterviewResponse.Start>> startInterview() {
        InterviewResponse.Start response = interviewService.startInterview();

        return ResponseEntity.ok(new CommonResponse<>(ResponseCode.SUCCESS, response));
    }
}
