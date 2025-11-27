//package techtrek.domain.session.service.common;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import techtrek.domain.session.dto.SessionParserResponse;
//
//import java.util.Set;
//
//@Component
//@RequiredArgsConstructor
//public class NumberCountProvider {
//    private final RedisTemplate<String, String> redisTemplate;
//
//    @Value("${custom.redis.prefix.basic}")
//    private String basicPrefix;
//
//    @Value("${custom.redis.prefix.resume}")
//    private String resumePrefix;
//
//    @Value("${custom.redis.prefix.tail}")
//    private String tailPrefix;
//
//    // questionNumber, count 계산
//    public SessionParserResponse.NumberCount exec(String sessionKey){
//        long basicCount = HashCount(sessionKey + basicPrefix + "*");
//        long resumeCount = HashCount(sessionKey + resumePrefix + "*");
//        long tailCount = HashCount(sessionKey + tailPrefix + "*");
//        String questionNumber = String.valueOf(basicCount + resumeCount + 1);
//        String currentCount = String.valueOf(basicCount + resumeCount + tailCount +1);
//        long totalCount = basicCount + resumeCount + tailCount;
//
//        return new SessionParserResponse.NumberCount(questionNumber, currentCount, totalCount);
//
//    }
//
//    // hash 개수 계산
//    public long HashCount(String pattern) {
//        Set<String> keys = redisTemplate.keys(pattern);
//        return keys != null ? keys.size() : 0;
//    }
//}
