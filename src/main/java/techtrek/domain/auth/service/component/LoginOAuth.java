package techtrek.domain.auth.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.auth.dto.CustomOAuthDetails;
import techtrek.domain.auth.service.common.GetAccessToken;
import techtrek.domain.auth.service.common.GetOAuthUserInfo;
import techtrek.domain.user.entity.Role;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.provider.JwtProvider;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class LoginOAuth {
    private final UserRepository userRepository;
    private final GetAccessToken getAccessToken;
    private final GetOAuthUserInfo getOAuthUserInfo;
    private final JwtProvider jwtProvider;

    public String exec(String provider, String code){
        // 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken.exec(provider, code);

        // 액세스 토큰으로 사용자 정보 요청
        CustomOAuthDetails userInfo = getOAuthUserInfo.exec(provider, accessToken);

        // 이메일로 기존 사용자 조회
        String email = userInfo.getEmail();
        User user = userRepository.findByEmail(email).orElse(null);

        // 신규 사용자면 저장
        if (user == null) {
            user = User.builder()
                    .id(provider + "_" + accessToken)
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
        String jwt = jwtProvider.createToken(user.getId());

        return String.format(
                "http://localhost:5173/social-login/callback?token=%s&name=%s",
                jwt,
                java.net.URLEncoder.encode(user.getName(), java.nio.charset.StandardCharsets.UTF_8)
        );
    }
}
