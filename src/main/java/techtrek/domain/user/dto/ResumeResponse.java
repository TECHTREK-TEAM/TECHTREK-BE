package techtrek.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeResponse {
        private String group;
        private String seniority;
        private String resume;
        private List<StackInfo> stacks;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class StackInfo {
                private String stackName;
        }
}

