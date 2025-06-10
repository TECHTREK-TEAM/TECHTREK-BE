package techtrek.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.service.ResumeService;
import techtrek.domain.user.service.UserService;
import techtrek.global.common.response.ApiResponse;
import techtrek.global.common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final ResumeService resumeService;

    // 사용자 불러오기
    @GetMapping("/info")
    public ResponseEntity<CommonResponse<UserResponse.Info>> getUser(){
        return ApiResponse.onSuccess(userService.getUser());
    }

    // 이력서 셍성
    @PostMapping("/resume")
    public ResponseEntity<CommonResponse<ResumeResponse>> createResume(@RequestPart MultipartFile file){
        return ApiResponse.onSuccess(resumeService.createResume(file));
    }

}
