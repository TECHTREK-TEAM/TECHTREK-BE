package techtrek.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.domain.user.service.ResumeService;
import techtrek.global.common.response.ApiResponse;
import techtrek.global.common.response.CommonResponse;


import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final ResumeService resumeService;

    // 이력서 업로드
    @PostMapping("/resume")
    public ResponseEntity<CommonResponse<ResumeResponse>> createResume(@RequestPart MultipartFile file) throws IOException {
        ResumeResponse response = resumeService.createResume(file);

        return ApiResponse.onSuccess(response);
    }}
