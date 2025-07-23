package techtrek.domain.auth.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.auth.dto.CustomOAuthDetails;
import techtrek.domain.auth.service.common.GetAccessToken;
import techtrek.domain.auth.service.common.GetOAuthUserInfo;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.GetUserByEmailDAO;
import techtrek.domain.user.service.small.SaveUserDAO;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LoginOAuthBean {
    private final GetAccessToken getAccessToken;
    private final GetOAuthUserInfo getOAuthUserInfo;
    private final GetUserByEmailDAO getUserByEmailDAO;
    private final SaveUserDAO saveUserDAO;

    public String exec(String provider, String code){
        // 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken.exec(provider, code);

        // 액세스 토큰으로 사용자 정보 요청
        CustomOAuthDetails userInfo = getOAuthUserInfo.exec(provider, accessToken);

        // 이메일로 기존 사용자 조회
        String email = userInfo.getEmail();
        User user = getUserByEmailDAO.exec(email);

        // 존재하지 않으면 사용자 저장
        if (user == null) { user = saveUserDAO.exec(provider, email, userInfo.getName(), accessToken); }
        // 존재하면 provider 비교 후 다르면 예외처리
        else { if (!user.getProvider().equals(provider)) { throw new CustomException(ErrorCode.LOGIN_ALREADY_EXISTS); } }

        System.out.println("user.id = " + user.getId());
        System.out.println("user.name = " + user.getName());
        return "true";
    }
}
