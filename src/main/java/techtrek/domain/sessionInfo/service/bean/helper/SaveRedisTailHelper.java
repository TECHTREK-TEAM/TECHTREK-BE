package techtrek.domain.sessionInfo.service.bean.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;
import techtrek.domain.sessionInfo.service.bean.small.CreateRedisTailDTOBean;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class SaveRedisTailHelper {
    private final RedisTemplate<String, String> redisTemplate;
    private final CreateRedisTailDTOBean createRedisTailDTOBean;

    // redis에 꼬리질문 저장
    public void exec( String tailSessionKey, String sessionKey, String fieldId, String question, String newTailCount){

        // 꼬리질문 개수 조회 (꼬리질문 리스트 크기)
        Long newCount = redisTemplate.opsForList().size(sessionKey);
        Long tailCountCurrent = redisTemplate.opsForList().size(tailSessionKey);
        tailCountCurrent = (tailCountCurrent == null) ? 0 : tailCountCurrent;

        // 전체 질문 개수 (기본 질문 + 꼬리질문)
        Long totalCount = (newCount == null ? 0 : newCount) + tailCountCurrent + 1;

        // 꼬리질문 저장용 DTO 생성
        String totalQuestionCount = String.valueOf(totalCount);
        RedisRequest.TailQuestion dto = createRedisTailDTOBean.exec(fieldId, question, newTailCount, totalQuestionCount);

        // JSON 직렬화
        String jsonString;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonString = objectMapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.REDIS_JSON_SERIALIZE_FAILED);
        }

        // Redis 꼬리질문 리스트에 저장
        redisTemplate.opsForList().rightPush(tailSessionKey, jsonString);
    }
}
