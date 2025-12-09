package techtrek.domain.auth.service.component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.auth.service.helper.RefreshTokenHelper;
import techtrek.global.securty.provider.JwtProvider;

@Component
@RequiredArgsConstructor
public class Logout {

    private final RefreshTokenHelper refreshTokenHelper;
    private final JwtProvider jwtProvider;

    // 로그아웃
    public Boolean exec(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = resolveRefreshToken(request);

        // redis 삭제
        if (refreshToken != null) {
            String userId = jwtProvider.getSubject(refreshToken);
            refreshTokenHelper.delete(userId);
        }

        // 클라이언트 쿠키 삭제
        Cookie cookie = new Cookie("refreshToken", null);
        cookie.setPath("/");              // 쿠키 범위
        cookie.setHttpOnly(true);         // JS 접근 불가
        cookie.setMaxAge(0);              // 만료시켜서 삭제
        response.addCookie(cookie);

        return true;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie cookie : request.getCookies()) {
            if ("refreshToken".equals(cookie.getName())) { // 대소문자 정확히 일치
                return cookie.getValue();
            }
        }
        return null;
    }
}
