package techtrek.global.gpt.prompt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class JsonRead {
    private final ObjectMapper objectMapper;

    // JSON → 객체(역직렬화, 파싱)
    public <T> T exec(String dto, Class<T> clazz) {
        try {
            return objectMapper.readValue(dto, clazz);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.JSON_READ_FAILED);
        }
    }
}
