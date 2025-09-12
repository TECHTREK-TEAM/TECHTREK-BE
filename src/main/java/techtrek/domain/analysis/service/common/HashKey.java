//package techtrek.domain.analysis.service.common;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.core.Cursor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ScanOptions;
//import org.springframework.stereotype.Component;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Component
//@RequiredArgsConstructor
//public class HashKey {
//    private final RedisTemplate<String, String> redisTemplate;
//
//    // 키를 이용하여 해시 데이터 조회
//    public Set<String> exec(String pattern) {
//        return redisTemplate.execute((RedisConnection connection) -> {
//            // 결과를 담을 Set
//            Set<String> keys = new HashSet<>();
//
//            // redis의 scan 명령어를 사용하여 커서 기반으로 키 조회
//            Cursor<byte[]> cursor = connection.scan(
//                    ScanOptions.scanOptions().match(pattern).count(1000).build()
//            );
//
//            // 커서를 통해 키들을 순회하면서 문자열로 변환 후 Set에 추가
//            while (cursor.hasNext()) {
//                keys.add(new String(cursor.next()));
//            }
//            return keys;
//        });
//    }
//}
//
