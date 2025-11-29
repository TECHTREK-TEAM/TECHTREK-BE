package techtrek.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techtrek.domain.auth.service.AuthService;
import techtrek.global.common.response.ApiResponse;
import techtrek.global.common.response.CommonResponse;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "인증 API", description = "인증 관련 API")
public class AuthController {
    private final AuthService authService;

    // 콜백 URI 처리
    @GetMapping("/auth/{provider}/callback")
    @Operation(summary = "콜백 조회(프론트 x)")
    public void oauthCallback(@RequestParam String code, @PathVariable String provider,
                              HttpServletResponse response) throws IOException {
        String redirectUrl = authService.login(provider, code,response);
        response.sendRedirect(redirectUrl);
    }

    // refresh token을 이용해 token 재발급
    @PostMapping("/auth/refresh")
    public ResponseEntity<CommonResponse<Map<String, String>>> refreshAccessToken(HttpServletRequest request) {
        String newAccessToken = authService.createTokenWithRefresh(request);
        return ApiResponse.onSuccess(Map.of("accessToken", newAccessToken));
    }

    // 로그아웃
    @PostMapping("/api/logout")
    public ResponseEntity<CommonResponse<Boolean>> logout(HttpServletRequest request, HttpServletResponse response) {
        return ApiResponse.onSuccess( authService.logout(request, response));
    }
}
