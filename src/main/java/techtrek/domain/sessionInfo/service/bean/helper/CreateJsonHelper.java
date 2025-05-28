package techtrek.domain.sessionInfo.service.bean.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class CreateJsonHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Json 변환 (직렬화)
    public String exec(RedisRequest.NewQuestion map) {
        try {
            return objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.REDIS_JSON_FAILD);
        }
    }
}
