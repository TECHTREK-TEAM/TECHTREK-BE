package techtrek.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;

import java.util.List;

public class UserResponse {

    // 사용자 정보
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String name;
        private String userGroup;
        private String seniority;
        private List<Stack> stacks;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Stack {
            private String stackName;
        }
    }

    // 관심 기업
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CompanyList {
        private List<Company> companies;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Company {
            private EnterpriseName companyName;
            private Double companyPercent;
        }
    }


    // 합격률
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonPropertyOrder({"interviewTotal", "interviewPass", "interviewPercent"})
    public static class Pass {
        private int interviewTotal;
        private int interviewPass;
        private Double interviewPercent;
    }

    // 일치율
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Score {
        private Double averageResultScore;
        private Double enhancedPercent;
    }

    // 면접
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Interview {
        private InterviewData highestScore;
        private InterviewData recentInterview;
        private Resume resume;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class InterviewData {
            private boolean status;
            private EnterpriseName enterpriseName;
            private Double resultScore;
            private String analysisGroup;
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Resume {
            private boolean status;
        }
    }


    // 이력서
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Resume {
        private String group;
        private String seniority;
        private String resume;
        private List<Stack> stacks;

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Stack {
            private String stackName;
        }
    }

}
