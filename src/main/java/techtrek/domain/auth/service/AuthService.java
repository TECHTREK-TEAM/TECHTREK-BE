package techtrek.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.auth.service.component.CreateTokenWithRefresh;
import techtrek.domain.auth.service.component.Login;
import techtrek.domain.auth.service.component.Logout;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final Login login;
    private final CreateTokenWithRefresh createTokenWithRefresh;
    private final Logout logout;

    // 소셜 로그인
    public String login(String provider, String code, HttpServletResponse response) {
        return login.exec(provider, code, response);
    }

    // refresh token을 이용해 token 재발급
    public String createTokenWithRefresh(HttpServletRequest request) {
        return createTokenWithRefresh.exec(request);
    }

    // 로그아웃
    public Boolean logout(HttpServletRequest request, HttpServletResponse response) {
        return logout.exec(request, response);
    }
}
