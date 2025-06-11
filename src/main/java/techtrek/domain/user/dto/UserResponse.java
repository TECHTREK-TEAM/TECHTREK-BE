package techtrek.domain.user.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.*;
import java.util.List;

public class UserResponse {

    // 사용자 정보
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Info {
        private String name;
        private String userGroup;
        private String seniority;
        private List<Stack> stacks;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Stack {
        private String stackName;
    }

    // 관심 기업
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CompanyList {
        private List<Company> companies;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Company {
        private String companyName;
        private Double companyPercent;
    }

    // 합격률
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonPropertyOrder({"interviewTotal", "interviewPass", "interviewPercent"})
    public static class Pass {
        private int InterviewTotal;
        private int interviewPass;
        private Double interviewPercent;
    }

    // 일치율
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Score {
        private Double averageResultScore;
        private Double enhancedPercent;
    }

}
