package techtrek.domain.sessionInfo.service.bean.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;
import techtrek.domain.sessionInfo.dto.RedisResponse;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetRedisPreviousHelper {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // 이전 질문의 phase, count값 불러오기
    public RedisResponse.PhaseCount exec(String sessionKey){
        // 이전 질문 불러오기
        List<String> previousList = redisTemplate.opsForList().range(sessionKey + ":new", -1, -1);
        String previousJsonString = previousList.isEmpty() ? null : previousList.get(0);

        // 이전 질문이 없을경우 예외처리
        if (previousJsonString == null) throw new CustomException(ErrorCode.PREVIOUS_QUESTION_NOT_FOUND);

        // 이전 질문의 phase, count값 불러오기
        String phase;
        int count = 0;

        try {
            RedisRequest.NewQuestion lastQuestion = objectMapper.readValue(previousJsonString, RedisRequest.NewQuestion.class);
            phase = lastQuestion.getPhase();
            count = Integer.parseInt(lastQuestion.getCount());

            // 만약 count가 5이상이라면, 기본질문 <-> 이력서 기반 질문 변경
            if (count >= 5) {
                phase = phase.equals("basic") ? "resume" : "basic";
                count = 0;
            }
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.REDIS_JSON_DESERIALIZE_FAILED);
        }

        return new RedisResponse.PhaseCount(phase, count);
    }

}
