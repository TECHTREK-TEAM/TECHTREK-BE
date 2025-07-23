package techtrek.domain.auth.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.auth.dto.CustomOAuthDetails;
import techtrek.domain.auth.service.common.GetAccessToken;
import techtrek.domain.auth.service.common.GetOAuthUserInfo;

@Component
@RequiredArgsConstructor
public class LoginOAuthBean {
    private final GetAccessToken getAccessToken;
    private final GetOAuthUserInfo getOAuthUserInfo;

    public String exec(String provider, String code){
        // 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken.exec(provider, code);

        // 액세스 토큰으로 사용자 정보 요청
        CustomOAuthDetails userInfo = getOAuthUserInfo.exec(provider, accessToken);

        System.out.println("userInfo.id = " + userInfo.getId());
        System.out.println("userInfo.name = " + userInfo.getName());
        return "true";
    }
}
