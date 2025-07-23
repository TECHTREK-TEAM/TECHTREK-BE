package techtrek.domain.auth.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.auth.service.common.GetAccessToken;

@Component
@RequiredArgsConstructor
public class LoginOAuthBean {
    private final GetAccessToken getAccessToken;

    public String exec(String provider, String code){
        // 인가 코드로 액세스 토큰 요청
        String accessToken = getAccessToken.exec(provider, code);

        System.out.println("accessToken = " + accessToken);

        return "true";
    }
}
