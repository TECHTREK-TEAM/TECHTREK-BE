package techtrek.domain.auth.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RefreshTokenManager {

    private final StringRedisTemplate redisTemplate;

    private static final String PREFIX = "RT:";

    // refresh token 저장
    public void save(String userId, String refreshToken, long ttlMillis) {
        redisTemplate.opsForValue().set(
                PREFIX + userId,
                refreshToken,
                ttlMillis,
                TimeUnit.MILLISECONDS
        );
    }

//    /**
//     * Refresh Token 조회
//     */
//    public String find(String userId) {
//        return redisTemplate.opsForValue().get(PREFIX + userId);
//    }
//
//    /**
//     * Refresh Token 검증
//     * - 저장된 RT와 클라이언트가 보낸 RT가 같은지 확인
//     */
//    public boolean validate(String userId, String refreshToken) {
//        String stored = find(userId);
//        return stored != null && stored.equals(refreshToken);
//    }
//
//    /**
//     * Refresh Token 삭제 (로그아웃 시)
//     */
//    public void delete(String userId) {
//        redisTemplate.delete(PREFIX + userId);
//    }
}