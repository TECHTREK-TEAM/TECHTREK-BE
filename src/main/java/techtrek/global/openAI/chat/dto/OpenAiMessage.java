package techtrek.global.openAI.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OpenAiMessage {
    private String role; // "user", "system", "assistant"
    private String content;
}