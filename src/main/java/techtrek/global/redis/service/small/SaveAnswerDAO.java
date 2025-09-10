//package techtrek.global.redis.service.small;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//
//@Component
//@RequiredArgsConstructor
//public class SaveAnswerDAO {
//    private final RedisTemplate<String, String> redisTemplate;
//
//    // 답변 저장
//    public void exec(String fieldKey, String answer){
//        redisTemplate.opsForHash().put(fieldKey, "answer", answer);
//    }
//}
