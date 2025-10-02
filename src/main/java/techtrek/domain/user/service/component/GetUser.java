package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetUser {
    private final UserValidator userValidator;

    // 사용자 정보 조회
    public UserResponse.Info exec(CustomUserDetails userDetails) {
        // 사용자 조회
        User user = userValidator.validateAndGetUser(userDetails.getId());

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
                .position(user.getPosition())
                .seniority(user.getSeniority())
                .stacks(stackDTOs)
                .build();
    }
}
