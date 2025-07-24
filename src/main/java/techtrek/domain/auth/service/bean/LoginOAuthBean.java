package techtrek.domain.auth.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.auth.dto.AuthResponse;
import techtrek.domain.auth.dto.CustomOAuthDetails;
import techtrek.domain.auth.service.common.GetAccessToken;
import techtrek.domain.auth.service.common.GetOAuthUserInfo;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.GetUserByEmailDAO;
import techtrek.domain.user.service.small.SaveUserDAO;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.provider.JwtProvider;

@Component
@RequiredArgsConstructor
public class LoginOAuthBean {
    private final GetAccessToken getAccessToken;
    private final GetOAuthUserInfo getOAuthUserInfo;
    private final GetUserByEmailDAO getUserByEmailDAO;
    private final SaveUserDAO saveUserDAO;
    private final JwtProvider jwtProvider;

    public AuthResponse.Login exec(String provider, String code){
        // 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken.exec(provider, code);

        // 액세스 토큰으로 사용자 정보 요청
        CustomOAuthDetails userInfo = getOAuthUserInfo.exec(provider, accessToken);

        // 이메일로 기존 사용자 조회
        String email = userInfo.getEmail();
        User user = getUserByEmailDAO.exec(email);

        // 신규 사용자면 저장
        if (user == null) {
            user = saveUserDAO.exec(provider, userInfo.getName(), email, accessToken);
        }
        // 기존 사용자인데 provider가 다르면 예외
        else if (!user.getProvider().equals(provider)) {
            throw new CustomException(ErrorCode.LOGIN_ALREADY_EXISTS);
        }
        // JWT 생성
        String jwt = jwtProvider.createToken(user.getId());

        return new AuthResponse.Login(jwt,user.getName());
    }
}
