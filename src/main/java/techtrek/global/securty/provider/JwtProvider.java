package techtrek.global.securty.provider;

import io.jsonwebtoken.*;
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
        Date now = new Date();
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

    // JWT에서 사용자 식별자(subject) 추출
    public String getSubject(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token);
            return claims.getBody().getSubject();
        } catch (JwtException e) {
            // 토큰이 유효하지 않을 때 null 반환
            return null;
        }
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}

