package techtrek.domain.auth.service.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
public class OAuthAuthUrlHelper {

    // ===== Kakao =====
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    // ===== Naver =====
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    // ===== Google =====
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    public String build(String provider, String state) {

        if ("kakao".equalsIgnoreCase(provider)) {
            return UriComponentsBuilder
                    .fromHttpUrl("https://kauth.kakao.com/oauth/authorize")
                    .queryParam("client_id", kakaoClientId)
                    .queryParam("redirect_uri", kakaoRedirectUri)
                    .queryParam("response_type", "code")
                    .queryParam("state", state)
                    .build()
                    .toUriString();
        }

        if ("naver".equalsIgnoreCase(provider)) {
            return UriComponentsBuilder
                    .fromHttpUrl("https://nid.naver.com/oauth2.0/authorize")
                    .queryParam("client_id", naverClientId)
                    .queryParam("redirect_uri", naverRedirectUri)
                    .queryParam("response_type", "code")
                    .queryParam("state", state)
                    .build()
                    .toUriString();
        }

        if ("google".equalsIgnoreCase(provider)) {
            return UriComponentsBuilder
                    .fromHttpUrl("https://accounts.google.com/o/oauth2/v2/auth")
                    .queryParam("client_id", googleClientId)
                    .queryParam("redirect_uri", googleRedirectUri)
                    .queryParam("response_type", "code")
                    .queryParam("scope", "openid profile email")
                    .queryParam("state", state)
                    .build()
                    .toUriString();
        }

        throw new CustomException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND);
    }
}
