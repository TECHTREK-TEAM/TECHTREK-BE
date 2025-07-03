package techtrek.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

public class UserRequest {

    // 사용자 정보
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "사용자 정보 요청")
    public static class Info {
        @Schema(description = "사용자 닉네임", example = "김철수")
        private String name;

        @Schema(description = "사용자 직군", example = "Frontend Developer")
        private String userGroup;

        @Schema(description = "연차 정보", example = "3년차")
        private String seniority;

        @Schema(description = "사용 기술 스택 리스트", example = "[{\"stackName\": \"React\"}, {\"stackName\": \"Spring Boot\"}]")
        private List<Stack> stacks;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Stack {
            @Schema(description = "기술 스택 이름", example = "Spring Boot")
            private String stackName;
        }
    }


}
