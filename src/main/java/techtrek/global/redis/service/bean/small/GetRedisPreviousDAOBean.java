package techtrek.global.redis.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisResponse;

@Component
@RequiredArgsConstructor
public class GetRedisPreviousDAOBean {
    private final RedisTemplate<String, String> redisTemplate;

    // 이전 질문 데이터 조회
    public RedisResponse.PreviousData exec(String previousKey) {
        Object phaseObject = redisTemplate.opsForHash().get(previousKey, "phase");
        String phase = phaseObject != null ? phaseObject.toString() : "basic";
        System.out.println("phase = " + phase);

        Object countObject = redisTemplate.opsForHash().get(previousKey, "count");
        String count = countObject != null ? countObject.toString() : "1";
        System.out.println("count = " + count);

        Object questionObject = redisTemplate.opsForHash().get(previousKey, "question");
        String question = questionObject != null ? questionObject.toString() : null;
        System.out.println("question = " + question);

        Object answerObject = redisTemplate.opsForHash().get(previousKey, "answer");
        String answer = answerObject != null ? answerObject.toString() : null;
        System.out.println("answer = " + answer);

        Object questionNumberObject = redisTemplate.opsForHash().get(previousKey, "questionNumber");
        String questionNumber = questionNumberObject != null ? questionNumberObject.toString() : "1";
        System.out.println("questionNumber = " + questionNumber);

        Object totalQuestionNumberObject = redisTemplate.opsForHash().get(previousKey, "totalQuestionNumber");
        String totalQuestionNumber = totalQuestionNumberObject != null ? totalQuestionNumberObject.toString() : null;
        System.out.println("totalQuestionNumber = " + totalQuestionNumber);

        return new RedisResponse.PreviousData(phase, count, question, answer, questionNumber, totalQuestionNumber);
    }
}
