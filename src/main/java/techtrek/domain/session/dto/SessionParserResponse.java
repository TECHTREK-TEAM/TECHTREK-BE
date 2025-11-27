package techtrek.domain.session.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SessionParserResponse {

    // GPT 반환
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatResult {
        private String question;
        private String correctAnswer;
    }

    // NumberCount 반환
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NumberCount {
        private String questionNumber;
        private String currentCount; // 총 개수 +1
        private long totalCount;  // 총 개수
    }

}
