package techtrek.domain.sessionInfo.service.bean.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;
import techtrek.domain.sessionInfo.service.bean.small.CreateRedisNewDTOBean;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class SaveRedisNewHelper {
    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final CreateRedisNewDTOBean createRedisNewDTOBean;

    // redis에 새로운 질문 저장
    public String exec(String sessionKey, String fieldId, String basicQuestion, String count, String phase) {
        // 새로운 질문 번호 계산 (기본질문 + 이력서 질문)
        Long currentQuestionCount = redisTemplate.opsForList().size(sessionKey + ":new");
        String questionNumber = String.valueOf(currentQuestionCount + 1);

        // 전체 질문 개수 계산 (새로운 질문 + 꼬리질문)
        Long currentTotalCount = redisTemplate.opsForHash().size(sessionKey);
        String totalQuestionCount = String.valueOf(currentTotalCount + 1);

        // 새로운 질문 DTO 생성
        RedisRequest.NewQuestion newData = createRedisNewDTOBean.exec(fieldId, basicQuestion, questionNumber, count,phase, totalQuestionCount);

        // JSON 문자열로 변환
        String jsonString;
        try {
            jsonString = objectMapper.writeValueAsString(newData);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.REDIS_JSON_SERIALIZE_FAILED);
        }

        // Redis에 저장
        redisTemplate.opsForList().rightPush(sessionKey + ":new", jsonString);

        return questionNumber;
    }
}
