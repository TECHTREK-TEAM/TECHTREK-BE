package techtrek.domain.user.service.bean.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.gpt.service.bean.CreateGPTBean;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class CreateResumeHelper {
    private final CreateGPTBean createGPTBean;
    private final ObjectMapper objectMapper;


    public ResumeResponse exec(String extractedText){
        // 프롬프트 생성
        String promptTemplate;
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("prompts/resume_summary_prompt.txt")) {
            if (is == null) throw new CustomException(ErrorCode.PROMPT_NOT_FOUND);
            promptTemplate = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.PROMPT_READ_FAILED);
        }

        // GPT 호출
        String prompt = String.format(promptTemplate, extractedText);
        String gptResponse = createGPTBean.exec(prompt);

        // JSON 파싱
        try {
            return objectMapper.readValue(gptResponse, ResumeResponse.class);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.PROMPT_PARSING_FAILED);
        }

    }
}
