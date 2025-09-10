//package techtrek.global.redis.service.small;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.stereotype.Component;
//import techtrek.domain.Interview.dto.SessionParserResponse;
//
//@Component
//@RequiredArgsConstructor
//public class GetRedisDAO {
//    private final RedisTemplate<String, String> redisTemplate;
//
//    // 데이터 조회
//    public SessionParserResponse.FieldData exec(String key) {
//        Object phaseObject = redisTemplate.opsForHash().get(key, "phase");
//        String phase = phaseObject != null ? phaseObject.toString() : "basic";
//
//        Object countObject = redisTemplate.opsForHash().get(key, "count");
//        String count = countObject != null ? countObject.toString() : "1";
//
//        Object questionObject = redisTemplate.opsForHash().get(key, "question");
//        String question = questionObject != null ? questionObject.toString() : null;
//
//        Object answerObject = redisTemplate.opsForHash().get(key, "answer");
//        String answer = answerObject != null ? answerObject.toString() : null;
//
//        Object questionNumberObject = redisTemplate.opsForHash().get(key, "questionNumber");
//        String questionNumber = questionNumberObject != null ? questionNumberObject.toString() : "1";
//
//        Object totalQuestionNumberObject = redisTemplate.opsForHash().get(key, "totalQuestionNumber");
//        String totalQuestionNumber = totalQuestionNumberObject != null ? totalQuestionNumberObject.toString() : null;
//
//        return new SessionParserResponse.FieldData(phase, count, question, answer, questionNumber, totalQuestionNumber);
//    }
//}
