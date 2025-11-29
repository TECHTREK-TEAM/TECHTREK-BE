package techtrek.domain.auth.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class OAuthTokenHelper {

    // 카카오
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String kakaoClientId;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String kakaoRedirectUri;

    // 네이버
    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String naverClientId;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String naverClientSecret;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String naverRedirectUri;

    // 구글
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String googleClientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String googleRedirectUri;

    // 인가코드(code)로 액세스 토큰 요청
    public String exec(String provider, String code) {
        // 카카오
        if ("kakao".equalsIgnoreCase(provider)) {
            // 카카오용 요청 파라미터 세팅
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", kakaoClientId);
            params.add("redirect_uri", kakaoRedirectUri);
            params.add("code", code);

            String tokenUrl = "https://kauth.kakao.com/oauth/token";
            return requestAccessToken(tokenUrl, params);

        }
        // 네이버
        else if ("naver".equalsIgnoreCase(provider)) {
            // 네이버용 요청 파라미터 세팅
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", naverClientId);
            params.add("client_secret", naverClientSecret);
            params.add("redirect_uri", naverRedirectUri);
            params.add("code", code);

            String tokenUrl = "https://nid.naver.com/oauth2.0/token";
            return requestAccessToken(tokenUrl, params);

        }
        // 구글
        else if ("google".equalsIgnoreCase(provider)) {
            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("grant_type", "authorization_code");
            params.add("client_id", googleClientId);
            params.add("client_secret", googleClientSecret);
            params.add("redirect_uri", googleRedirectUri);
            params.add("code", code);

            String tokenUrl = "https://oauth2.googleapis.com/token";
            return requestAccessToken(tokenUrl, params);
        } else {
            throw new CustomException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND);
        }
    }

    // 토큰 요청 메서드
    private String requestAccessToken(String tokenUrl, MultiValueMap<String, String> params) {
        RestTemplate restTemplate = new RestTemplate();

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // 토큰 발급 요청
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(tokenUrl, requestEntity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            Map<String, Object> body = response.getBody();
            return (String) body.get("access_token");
        } else {
            throw new CustomException(ErrorCode.OAUTH_TOKEN_REQUEST_FAILED);
        }
    }
}
