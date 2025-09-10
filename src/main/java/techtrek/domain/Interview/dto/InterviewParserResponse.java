package techtrek.domain.Interview.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class InterviewParserResponse {

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
