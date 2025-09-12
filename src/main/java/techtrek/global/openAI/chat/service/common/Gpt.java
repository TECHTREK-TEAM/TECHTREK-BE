package techtrek.global.openAI.chat.service.common;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Gpt {
    private final Prompt prompt;
    private final GptApi gptApi;
    private final JsonRead jsonRead;

    // GPT 요청 후 DTO 반환
    public <T> T exec(String promptPath, Object[] args, Class<T> responseType) {
        // 프롬프트 불러오기
        String template = prompt.exec(promptPath);

        // 프롬프트 완성
        String formattedPrompt = String.format(template, args);

        // GPT 호출
        String chatResponse = gptApi.exec(formattedPrompt);

        // JSON → DTO
        return jsonRead.exec(chatResponse, responseType);
    }

}
