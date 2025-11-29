package techtrek.domain.auth.service.component;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import techtrek.domain.auth.dto.CustomOAuthDetails;
import techtrek.domain.auth.service.helper.OAuthTokenHelper;
import techtrek.domain.auth.service.helper.OAuthUserInfoHelper;
import techtrek.domain.auth.service.helper.RefreshTokenHelper;
import techtrek.domain.user.entity.Role;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.securty.provider.JwtProvider;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateOAuth {
    private final UserRepository userRepository;
    private final OAuthTokenHelper oAuthTokenHelper;
    private final OAuthUserInfoHelper oAuthUserInfoHelper;
    private final JwtProvider jwtProvider;
    private final RefreshTokenHelper refreshTokenHelper;

    public String exec(String provider, String code, HttpServletResponse response){
        // 인가 코드로 액세스 토큰 요청
        String oauthAccessToken = oAuthTokenHelper.exec(provider, code);

        // 액세스 토큰으로 사용자 정보 요청
        CustomOAuthDetails userInfo = oAuthUserInfoHelper.exec(provider, oauthAccessToken);

        // 이메일로 기존 사용자 조회
        String email = userInfo.getEmail();
        User user = userRepository.findByEmail(email).orElse(null);

        // 신규 사용자면 저장
        if (user == null) {
            user = User.builder()
                    .id(provider + "_" + oauthAccessToken)
                    .name(userInfo.getName())
                    .email(email)
                    .provider(provider)
                    .role(Role.USER)
                    .createdAt(LocalDateTime.now().withNano(0))
                    .build();

            user = userRepository.save(user); // 이제 user는 null이 아님
        }
        // 기존 사용자인데 provider가 다르면 예외
        else if (!user.getProvider().equals(provider)) {
            return String.format(
                    "http://localhost:5173/social-login/callback?message=%s",
                    java.net.URLEncoder.encode("이미 다른 로그인 방식으로 가입된 회원입니다.", java.nio.charset.StandardCharsets.UTF_8)
            );
        }
        // JWT 생성
        String accessToken = jwtProvider.createAccessToken(user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        // Refresh 저장
        refreshTokenHelper.save(user.getId(), refreshToken, jwtProvider.getRefreshTokenTTL());

        // Refresh Token을 쿠키로 내려주기
        ResponseCookie rtCookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(jwtProvider.getRefreshTokenTTL())
                .sameSite("None")
                .build();

        response.addHeader("Set-Cookie", rtCookie.toString());

        return String.format(
                "http://localhost:5173/social-login/callback?token=%s&name=%s",
                accessToken,
                java.net.URLEncoder.encode(user.getName(), java.nio.charset.StandardCharsets.UTF_8)
        );
    }
}
