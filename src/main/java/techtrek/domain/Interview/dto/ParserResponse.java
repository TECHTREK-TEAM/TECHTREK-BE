package techtrek.domain.Interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParserResponse {

    // GPT 반환
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatResult {
        private String question;
        private String correctAnswer;
    }

}
