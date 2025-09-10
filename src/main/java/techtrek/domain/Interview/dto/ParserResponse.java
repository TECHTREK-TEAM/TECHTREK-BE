package techtrek.domain.Interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ParserResponse {

    // 기본질문 GPT
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicQuestion {
        private String question;
        private String correctAnswer;
    }

    // 기본질문 반환
    @Getter
    @AllArgsConstructor
    public static class BasicQuestionResult {
        private final String question;
        private final String correctAnswer;
    }

}
