package techtrek.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class UserResponse {

    // 사용자 정보
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "사용자 정보 응답")
    public static class Info {
        @Schema(description = "사용자 닉네임", example = "김철수")
        private String name;

        @Schema(description = "사용자 직군", example = "Frontend Developer")
        private String position;

        @Schema(description = "연차 정보", example = "3년차")
        private String seniority;

        @Schema(description = "사용 기술 스택 리스트", example = "[{\"stackName\": \"React\"}, {\"stackName\": \"Spring Boot\"}]")
        private List<Stack> stacks;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Stack {
            @Schema(description = "기술 스택 이름", example = "Spring Boot")
            private String stackName;

            @Schema(description = "기술 스택 url", example = "https://..")
            private String stackUrl;
        }
    }

    // 관심 기업
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "관심 기업 리스트 응답")
    public static class CompanyList {
        @Schema(description = "관심 기업 목록")
        private List<Company> companies;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "관심 기업 정보")
        public static class Company {
            @Schema(description = "기업 이름", example = "카카오")
            private String companyName;

            @Schema(description = "해당 기업의 면접 일치율(%) 평균", example = "87.5")
            private Double avgScore;
        }
    }


    // 합격률
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"interviewTotal", "interviewPass", "interviewPercent"})
    @Schema(description = "사용자의 면접 합격률 정보 응답")
    public static class Pass {
        @Schema(description = "전체 면접 횟수", example = "10")
        private int interviewTotal;

        @Schema(description = "합격한 면접 횟수", example = "6")
        private int interviewPass;

        @Schema(description = "합격률 (%)", example = "60.0")
        private Double interviewPercent;
    }

    // 일치율
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "면접 결과 기반의 평균 일치율 정보 응답")
    public static class Score {
        @Schema(description = "면접 결과 점수 평균", example = "75.4")
        private Double totalAvgScore;

        @Schema(description = "이전보다 향상된 점수 비율 (%)", example = "12.5")
        private Double enhancedPercent;
    }

    // 면접
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "면접 정보 응답")
    public static class Interview {
        @Schema(description = "가장 높은 점수를 받은 면접 정보")
        private InterviewData highestScore;

        @Schema(description = "최근에 본 면접 정보")
        private InterviewData recentInterview;

        @Schema(description = "이력서 업로드 여부")
        private Resume resume;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "면접 데이터")
        public static class InterviewData {
            @Schema(description = "분석 ID", example = "true")
            private Long analysisId;

            @Schema(description = "면접 합격 여부", example = "true")
            private boolean isPass;

            @Schema(description = "기업 이름", example = "네이버")
            private String enterpriseName;

            @Schema(description = "면접 일치 점수", example = "88.2")
            private Double score;

            @Schema(description = "분석된 직군 정보", example = "Backend Developer")
            private String analysisPosition;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "이력서 상태")
        public static class Resume {
            @Schema(description = "이력서 존재 여부", example = "true")
            private boolean status;

            @Schema(description = "이력서 이름", example = "테크트랙 이력서.pdf")
            private String resumeName;
        }
    }

    // 이력서
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "사용자의 이력서 정보 응답")
    public static class Resume {
        @Schema(description = "사용자 직군", example = "Frontend Developer")
        private String position;

        @Schema(description = "연차 정보", example = "지망생")
        private String seniority;

        @Schema(description = "이력서 파일 URL 또는 텍스트 요약", example = "https://s3.bucket.com/resume123.pdf")
        private String resume;

        @Schema(description = "기술 스택 목록")
        private List<Stack> stacks;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        @Schema(description = "기술 스택")
        public static class Stack {
            @Schema(description = "스택 이름", example = "Spring Boot")
            private String stackName;

            @Schema(description = "스택 url", example = "http://~")
            private String stackUrl;
        }
    }

}
