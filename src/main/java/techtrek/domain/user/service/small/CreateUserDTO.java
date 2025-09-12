package techtrek.domain.user.service.small;

import org.springframework.stereotype.Component;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class CreateUserDTO {

    // 사용자 정보 조회 DTO
    public UserResponse.Info exec(User user) {

        // 스택을 list형태로 불러오기
        List<UserResponse.Info.Stack> stackDTOs = new ArrayList<>();
        for (Stack stack : user.getStackList()) {
            UserResponse.Info.Stack dto = UserResponse.Info.Stack.builder()
                    .stackName(stack.getStackName())
                    .build();
            stackDTOs.add(dto);
        }

        // dto 생성
        return UserResponse.Info.builder()
                .name(user.getName())
                .role(user.getRole())
                .seniority(user.getSeniority())
                .stacks(stackDTOs)
                .build();
    }
}
