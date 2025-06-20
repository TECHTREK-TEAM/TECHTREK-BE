package techtrek.global.util;

import org.springframework.stereotype.Component;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
public class CreatePromptTemplateUtil {

    // 프롬프트 템플릿 생성
    public String exec(String resourcePath) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) throw new CustomException(ErrorCode.PROMPT_NOT_FOUND);
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.JSON_READ_FAILED);
        }
    }
}
