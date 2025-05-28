package techtrek.domain.user.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeResponse {
        private String group;
        private String seniority;
        private String resume;
        private List<Stack> stacks;

        @Getter
        @Setter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class Stack {
                private String stackName;
        }
}

