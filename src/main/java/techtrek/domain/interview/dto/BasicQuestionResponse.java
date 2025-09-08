package techtrek.domain.interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BasicQuestionResponse {

    // 기본질문 GPT
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BasicQuestion {
        private String question;
        private String answer;
    }

    // 기본질문 반환
    @Getter
    @AllArgsConstructor
    public static class BasicQuestionResult {
        private final String question;
        private final String answer;
    }

}
