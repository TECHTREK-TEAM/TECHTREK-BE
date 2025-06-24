package techtrek.domain.user.dto;

import lombok.*;

import java.util.List;

public class UserRequest {

    // 사용자 정보
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String name;
        private String userGroup;
        private String seniority;
        private List<Stack> stacks;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Stack {
            private String stackName;
        }
    }


}
