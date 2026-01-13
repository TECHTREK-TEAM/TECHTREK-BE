package techtrek.domain.auth.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;
import techtrek.domain.auth.service.helper.OAuthAuthUrlHelper;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateState {
    private final OAuthAuthUrlHelper oAuthAuthUrlHelper;

    // state 랜덤 생성
    public String exec(String provider, HttpServletRequest request) {
        String state = UUID.randomUUID().toString();
        request.getSession().setAttribute("OAUTH_STATE", state);

        String redirectUrl = oAuthAuthUrlHelper.build(provider, state);

        return redirectUrl;
    }
}

