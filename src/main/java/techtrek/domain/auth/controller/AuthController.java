package techtrek.domain.auth.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import techtrek.domain.auth.service.AuthService;

@RestController
@RequiredArgsConstructor
@RequestMapping
@Tag(name = "인증 API", description = "인증 관련 API")
public class AuthController {
    private final AuthService authService;

    // 콜백 URI 처리
    @GetMapping("/auth/{provider}/callback")
    @Operation(summary = "콜백 조회(프론트 x)")
    public ResponseEntity<String> oauthCallback(@RequestParam String code, @PathVariable String provider) {
        return ResponseEntity.ok(authService.loginOAuth(provider, code));
    }
}
