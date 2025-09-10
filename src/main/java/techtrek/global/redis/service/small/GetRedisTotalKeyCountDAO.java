//package techtrek.global.redis.service.small;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//import java.util.Set;
//
//@Component
//@RequiredArgsConstructor
//public class GetRedisTotalKeyCountDAO {
//    private final RedisTemplate<String, String> redisTemplate;
//
//    // 총 질문 개수(키 개수) 조회
//    public int exec(String keyPrefix){
//        Set<String> keys = redisTemplate.keys(keyPrefix + "*");
//        if (keys == null) return 0;
//
//        return keys.size();
//    }
//}
