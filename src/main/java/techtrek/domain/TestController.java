package techtrek.domain;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import techtrek.global.securty.service.CustomUserDetails;

@Controller
public class TestController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // templates/login.html
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        // 인증된 사용자의 이름 반환
        return ResponseEntity.ok(userDetails.getName());
    }
}
