package techtrek.domain.analysis.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import techtrek.domain.interviewQuestion.entity.status.EnterpriseName;

import java.time.LocalDateTime;
import java.util.List;

public class AnalysisResponse {

    // 분석
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema(description = "분석 데이터 응답")
    public static class Analysis {
        @Schema(description = "분석 ID", example = "5678")
        private String analysisId;

        @Schema(description = "합격 여부", example = "true")
        private Boolean status;

        @Schema(description = "일치율 점수", example = "85.5")
        private Double resultScore;

        @Schema(description = "팔로우 점수", example = "90.0")
        private Double followScore;

        @Schema(description = "결과 설명", example = "우수")
        private String result;

        @Schema(description = "분석 소요 시간(분)", example = "5")
        private int duration;

        @Schema(description = "키워드", example = "협업, 리더십")
        private String keyword;
    }


    // 세션 불러오기
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "분석 세션 상세 정보 응답")
    public static class Detail{
        @Schema(description = "세션 정보 ID", example = "1234")
        private String sessionInfoId;

        @Schema(description = "분석 결과")
        private Analysis analysis;

        @Schema(description = "면접 리스트")
        private List<Interview> interview;

        @Schema(description = "피드백 정보")
        private Feedback feedback;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Analysis {
            @Schema(description = "합격 여부", example = "true")
            private boolean status;

            @Schema(description = "팔로우 점수", example = "75.0")
            private double followScore;

            @Schema(description = "평균대비 증가 비율", example = "80.5")
            private double averageFollowPercent;

            @Schema(description = "일치율 점수", example = "85.0")
            private double resultScore;

            @Schema(description = "분석 소요 시간(분)", example = "5")
            private int duration;

            @Schema(description = "평균대비 증가 비율", example = "90.0")
            private double averageDurationPercent;

            @Schema(description = "상위 퍼센트", example = "95.0")
            private double topScore;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Interview {
            @Schema(description = "질문 내용", example = "자기소개를 해주세요.")
            private String question;

            @Schema(description = "답변 내용", example = "저는 5년차 백엔드 개발자입니다.")
            private String answer;

            @Schema(description = "질문 번호", example = "1")
            private String questionNumber;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Feedback {
            @Schema(description = "키워드", example = "협업")
            private String keyword;

            @Schema(description = "키워드 번호", example = "1")
            private String keywordNumber;

            @Schema(description = "설명", example = "우수")
            private String result;
        }
    }

    // 세션 리스트 불러오기
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "분석 세션 리스트")
    public static class SessionList {
        @Schema(description = "세션 리스트")
        private List<SessionData> session;
    }

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SessionData {
        @Schema(description = "세션 정보 ID", example = "1234")
        private String sessionInfoId;

        @Schema(description = "기업명", example = "네이버")
        private EnterpriseName enterpriseName;

        @Schema(description = "생성일자", example = "2025-06-30 10:15:30")
        private LocalDateTime createdAt;
    }
}
