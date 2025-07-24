package techtrek.global.provider;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    // JWT 생성
    public String createToken(String userId) {
        Date now = new Date(); // 현재 시간
        Date expiryDate = new Date(now.getTime() + expiration); // 만료 시간

        // JWT 토큰 생성
        return Jwts.builder()
                .setSubject(userId)                  // 사용자 식별자 (userId)를 subject로 저장
                .setIssuedAt(now)                    // 발급 시간
                .setExpiration(expiryDate)           // 만료 시간
                .signWith(SignatureAlgorithm.HS256,  // 서명 알고리즘 + 시크릿 키로 서명
                        secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();                          // 최종 JWT 문자열 반환
    }
}

