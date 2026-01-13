package techtrek.domain.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.auth.service.component.CreateState;
import techtrek.domain.auth.service.component.CreateTokenWithRefresh;
import techtrek.domain.auth.service.component.Login;
import techtrek.domain.auth.service.component.Logout;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final CreateState createState;
    private final Login login;
    private final CreateTokenWithRefresh createTokenWithRefresh;
    private final Logout logout;

    // 소셜 로그인 state 생성
    public String createState(String provider, HttpServletRequest request) {
        return createState.exec(provider, request);
    }

    // 소셜 로그인
    public String login(String code, String state,String provider, HttpServletRequest request, HttpServletResponse response) {
        return login.exec(code, state, provider, request, response);
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
