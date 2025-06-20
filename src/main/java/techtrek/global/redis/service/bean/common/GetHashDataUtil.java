package techtrek.global.redis.service.bean.common;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GetHashDataUtil {
    private final RedisTemplate<String, String> redisTemplate;

    // key를 이용하여 해당하는 해시 데이터 조회
    public Set<String> exec(String pattern) {
        return redisTemplate.execute((RedisConnection connection) -> {
            Set<String> keys = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(
                    ScanOptions.scanOptions().match(pattern).count(1000).build()
            );
            while (cursor.hasNext()) {
                keys.add(new String(cursor.next()));
            }
            return keys;
        });
    }
}
