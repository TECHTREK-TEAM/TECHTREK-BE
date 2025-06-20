package techtrek.global.redis.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.global.redis.dto.RedisResponse;

@Component
@RequiredArgsConstructor
public class GetRedisDAO {
    private final RedisTemplate<String, String> redisTemplate;

    // 이전 질문 데이터 조회
    public RedisResponse.FieldData exec(String previousKey) {
        Object phaseObject = redisTemplate.opsForHash().get(previousKey, "phase");
        String phase = phaseObject != null ? phaseObject.toString() : "basic";

        Object countObject = redisTemplate.opsForHash().get(previousKey, "count");
        String count = countObject != null ? countObject.toString() : "1";

        Object questionObject = redisTemplate.opsForHash().get(previousKey, "question");
        String question = questionObject != null ? questionObject.toString() : null;

        Object answerObject = redisTemplate.opsForHash().get(previousKey, "answer");
        String answer = answerObject != null ? answerObject.toString() : null;

        Object questionNumberObject = redisTemplate.opsForHash().get(previousKey, "questionNumber");
        String questionNumber = questionNumberObject != null ? questionNumberObject.toString() : "1";

        Object totalQuestionNumberObject = redisTemplate.opsForHash().get(previousKey, "totalQuestionNumber");
        String totalQuestionNumber = totalQuestionNumberObject != null ? totalQuestionNumberObject.toString() : null;

        return new RedisResponse.FieldData(phase, count, question, answer, questionNumber, totalQuestionNumber);
    }
}
