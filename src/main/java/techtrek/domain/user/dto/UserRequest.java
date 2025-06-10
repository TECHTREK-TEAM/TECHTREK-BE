package techtrek.domain.user.dto;

import lombok.*;

import java.util.List;

public class UserRequest {

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
}
