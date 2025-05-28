package techtrek.domain.sessionInfo.service.bean.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CreateAnswerHelper {
    private final RedisTemplate<String, String> redisTemplate;

    // 답변
    public boolean exec(String sessionKey, String fieldId, String answer){
        // 해당 모든 질문 항목 조회
        List<String> jsonList = redisTemplate.opsForList().range(sessionKey, 0, -1);

        for (int i = 0; i < jsonList.size(); i++) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, String> data = objectMapper.readValue(jsonList.get(i), Map.class);

                // 존재한다면 답변 값 업데이트
                if (fieldId.equals(data.get("fieldId"))) {
                    data.put("answer", answer);

                    // JSON 직렬화
                    String updatedJson = objectMapper.writeValueAsString(data);
                    redisTemplate.opsForList().set(sessionKey, i, updatedJson);

                    return true;
                }
            } catch (JsonProcessingException e) {
                throw new CustomException(ErrorCode.REDIS_JSON_SERIALIZE_FAILED);
            }
        }

        // 찾지 못할 경우
        return false;

    }
}
