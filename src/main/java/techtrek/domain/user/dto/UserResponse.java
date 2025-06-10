package techtrek.domain.user.dto;

import lombok.*;
import java.util.List;

public class UserResponse {

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
        private double companyPercent;
    }


}
