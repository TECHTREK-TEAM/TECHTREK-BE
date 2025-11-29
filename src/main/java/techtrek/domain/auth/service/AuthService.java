package techtrek.domain.auth.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.auth.service.component.LoginOAuth;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final LoginOAuth loginOAuth;

    // 소셜 로그인
    public String loginOAuth(String provider, String code, HttpServletResponse response) {
        return loginOAuth.exec(provider, code, response);
    }
}
