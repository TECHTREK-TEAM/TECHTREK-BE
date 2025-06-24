package techtrek.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.UserRequest;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.service.UserService;
import techtrek.global.common.response.ApiResponse;
import techtrek.global.common.response.CommonResponse;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 사용자 정보 조회
    @GetMapping("/info")
    public ResponseEntity<CommonResponse<UserResponse.Info>> getUser(){
        return ApiResponse.onSuccess(userService.getUser());
    }

    // 사용자 정보 수정
    @PatchMapping("/info")
    public ResponseEntity<CommonResponse<UserResponse.Info>> updateUser(@RequestBody UserRequest.Info request){
        return ApiResponse.onSuccess(userService.updateUser(request));
    }

    // 관심기업 조회
    @GetMapping("/companies")
    public ResponseEntity<CommonResponse<UserResponse.CompanyList>> getCompany(){
        return ApiResponse.onSuccess(userService.getCompany());
    }

    // 합격률 조회
    @GetMapping("/pass")
    public ResponseEntity<CommonResponse<UserResponse.Pass>> getPass(){
        return ApiResponse.onSuccess(userService.getPass());
    }

    // 일치율 조회
    @GetMapping("/score")
    public ResponseEntity<CommonResponse<UserResponse.Score>> getScore(){
        return ApiResponse.onSuccess(userService.getScore());
    }

    // 면접 정보 조회
    @GetMapping("/interviews")
    public ResponseEntity<CommonResponse<UserResponse.Interview>> getInterview(){
        return ApiResponse.onSuccess(userService.getInterview());
    }

    // 이력서 셍성
    @PostMapping("/resume")
    public ResponseEntity<CommonResponse<UserResponse.Resume>> createResume(@RequestPart MultipartFile file){
        return ApiResponse.onSuccess(userService.createResume(file));
    }

}
