package techtrek.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.auth.service.bean.LoginOAuthBean;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final LoginOAuthBean loginOAuthBean;

    // 소셜 로그인
    public String loginOAuth(String provider, String code) {
        return loginOAuthBean.exec(provider, code);
    }
}
