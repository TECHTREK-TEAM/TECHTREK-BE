package techtrek.domain.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
@Tag(name = "사용자 API", description = "사용자 관련 API")
public class UserController {

    private final UserService userService;

    // 사용자 정보 조회
    @GetMapping("/info")
    @Operation(summary = "사용자 정보 조회", description = "현재 로그인한 사용자의 기본 정보를 조회합니다.")
    public ResponseEntity<CommonResponse<UserResponse.Info>> getUser(){
        return ApiResponse.onSuccess(userService.getUser());
    }

    // 사용자 정보 수정
    @PatchMapping("/info")
    @Operation(summary = "사용자 정보 수정", description = "사용자의 프로필 정보를 수정합니다.")
    public ResponseEntity<CommonResponse<UserResponse.Info>> updateUser(@RequestBody UserRequest.Info request){
        return ApiResponse.onSuccess(userService.updateUser(request));
    }

    // 관심기업 조회
    @GetMapping("/companies")
    @Operation(summary = "관심 기업 조회", description = "가장 많이 면접을 본 상위 3개 기업을 조회합니다.")
    public ResponseEntity<CommonResponse<UserResponse.CompanyList>> getCompany(){
        return ApiResponse.onSuccess(userService.getCompany());
    }
//
//    // 합격률 조회
//    @GetMapping("/pass")
//    @Operation(summary = "합격률 조회", description = "사용자의 전체 면접 합격률을 조회합니다.")
//    public ResponseEntity<CommonResponse<UserResponse.Pass>> getPass(){
//        return ApiResponse.onSuccess(userService.getPass());
//    }
//
//    // 일치율 조회
//    @GetMapping("/score")
//    @Operation(summary = "일치율 조회", description = "전체 면접 결과의 일치율 평균 점수를 조회합니다.")
//    public ResponseEntity<CommonResponse<UserResponse.Score>> getScore(){
//        return ApiResponse.onSuccess(userService.getScore());
//    }
//
//    // 면접 정보 조회
//    @GetMapping("/interviews")
//    @Operation(summary = "면접 정보 조회", description = "가장 높은 점수를 받은 면접과 가장 최근에 본 면접 정보를 조회합니다.")
//    public ResponseEntity<CommonResponse<UserResponse.Interview>> getInterview(){
//        return ApiResponse.onSuccess(userService.getInterview());
//    }
//
    // 이력서 셍성
    @PostMapping(value ="/resume", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "이력서 생성", description = "이력서 PDF 파일을 업로드하여 정보를 생성합니다.")
    public ResponseEntity<CommonResponse<UserResponse.Resume>> createResume(@RequestPart("file") MultipartFile file){
        return ApiResponse.onSuccess(userService.createResume(file));
    }

}
