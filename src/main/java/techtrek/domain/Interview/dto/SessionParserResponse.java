package techtrek.domain.Interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// redis 데이터를 파싱해 담는 dto
public class SessionParserResponse {

    // 필드 형태로 데이터 파싱
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldData {
        private String phase;
        private String count;
        private String question;
        private String answer;
        private String questionNumber;
        private String totalQuestionNumber;
    }

    // 리스트 형태로 데이터 파싱
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListData {
        private String question;
        private String answer;
        private String questionNumber;
        private String totalQuestionNumber;
        private String tailQuestionMessage;
    }
}
