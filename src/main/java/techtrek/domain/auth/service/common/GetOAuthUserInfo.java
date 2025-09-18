package techtrek.domain.auth.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import techtrek.domain.auth.dto.CustomOAuthDetails;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class GetOAuthUserInfo {

    private final RestTemplate restTemplate;

    // 액세스 토큰으로 사용자 정보 요청
    public CustomOAuthDetails exec(String provider, String accessToken) {
        // 카카오
        if ("kakao".equalsIgnoreCase(provider)) {
            // 응답 Nap 생성
            String userInfoUrl = "https://kapi.kakao.com/v2/user/me";
            Map<String, Object> userInfo = requestUserInfo(userInfoUrl, accessToken);
            return kakaoUserInfo(userInfo);
        }
        // 네이버
        else if ("naver".equalsIgnoreCase(provider)) {
            String userInfoUrl = "https://openapi.naver.com/v1/nid/me";
            Map<String, Object> userInfo = requestUserInfo(userInfoUrl, accessToken);
            return naverUserInfo(userInfo);
        }
        // 구글
        else if ("google".equalsIgnoreCase(provider)) {
            String userInfoUrl = "https://www.googleapis.com/oauth2/v3/userinfo";
            Map<String, Object> userInfo = requestUserInfo(userInfoUrl, accessToken);
            return googleUserInfo(userInfo);
        }
        throw new CustomException(ErrorCode.OAUTH_PROVIDER_NOT_FOUND);
    }

    // 공통 HTTP 요청 + 응답 Map 반환
    private Map<String, Object> requestUserInfo(String url, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new CustomException(ErrorCode.OAUTH_USERINFO_REQUEST_FAILED);
        }
    }

    // 카카오 정보 파싱
    private CustomOAuthDetails kakaoUserInfo(Map<String, Object> userInfo) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) userInfo.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return new CustomOAuthDetails(
                userInfo.get("id").toString(),
                (String) profile.get("nickname"),
                (String) kakaoAccount.get("email")
        );
    }

    // 네이버 정보 파싱
    private CustomOAuthDetails naverUserInfo(Map<String, Object> userInfo) {
        Map<String, Object> naverResponse = (Map<String, Object>) userInfo.get("response");

        return new CustomOAuthDetails(
                (String) naverResponse.get("id"),
                (String) naverResponse.get("name"),
                (String) naverResponse.get("email")
        );
    }

    // 구글 정보 파싱
    private CustomOAuthDetails googleUserInfo(Map<String, Object> userInfo) {
        return new CustomOAuthDetails(
                (String) userInfo.get("sub"),
                (String) userInfo.get("name"),
                (String) userInfo.get("email")
        );
    }
}

