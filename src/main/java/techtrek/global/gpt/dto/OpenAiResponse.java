package techtrek.global.gpt.dto;

import lombok.Data;

import java.util.List;

@Data
public class OpenAiResponse {
    private List<Choice> choices;

    @Data
    public static class Choice {
        private OpenAiMessage message;
    }
}
