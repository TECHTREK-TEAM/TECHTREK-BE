package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetUser {
    private final UserRepository userRepository;

    public UserResponse.Info exec() {
        // TODO:사용자 조회
        User user = userRepository.findById("1")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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
