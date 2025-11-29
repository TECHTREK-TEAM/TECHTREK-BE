package techtrek.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.auth.service.component.CreateTokenWithRefresh;
import techtrek.domain.auth.service.component.CreateOAuth;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CreateOAuth createOAuth;
    private final CreateTokenWithRefresh createTokenWithRefresh;

    // 소셜 로그인
    public String createOAuth(String provider, String code, HttpServletResponse response) {
        return createOAuth.exec(provider, code, response);
    }

    // refresh token을 이용해 token 재발급
    public String createTokenWithRefresh(HttpServletRequest request) {
        return createTokenWithRefresh.exec(request);
    }
}
