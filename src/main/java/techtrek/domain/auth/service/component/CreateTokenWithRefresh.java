package techtrek.domain.auth.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.auth.service.helper.RefreshTokenHelper;
import techtrek.global.securty.provider.JwtProvider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class CreateTokenWithRefresh {

    private final JwtProvider jwtProvider;
    private final RefreshTokenHelper refreshTokenHelper;

    // refresh token을 이용해 token 재발급
    public String exec(HttpServletRequest request) {
        // 쿠키에서 Refresh Token 가져오기
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new RuntimeException("Refresh token missing");
        }

        String refreshToken = Arrays.stream(cookies)
                .filter(c -> "refreshToken".equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Refresh token missing"));

        // JWT에서 userId 추출
        String userId = jwtProvider.getSubject(refreshToken);

        // Redis에서 검증
        if (userId == null || !jwtProvider.validateToken(refreshToken)
                || !refreshTokenHelper.validateByUserId(userId, refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        // 새 Access Token 발급
        return jwtProvider.createAccessToken(userId);
    }
}
