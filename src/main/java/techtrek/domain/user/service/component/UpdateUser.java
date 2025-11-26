package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.user.dto.UserRequest;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateUser {
    private final UserValidator userValidator;
    private final UserRepository userRepository;

    // 사용자 정보 수정
    public UserResponse.Info exec (String newName, String newPosition, String newSeniority, List<UserRequest.InfoRequest.Stack> newStacks, CustomUserDetails userDetails) {
        // 사용자 조회
        User user = userValidator.validateAndGetUser(userDetails.getId());

        // 사용자 정보 수정 저장
        if (newName != null && !newName.isBlank()) user.setName(newName);
        if (newPosition != null) user.setPosition(newPosition);
        if (newSeniority != null) user.setSeniority(newSeniority);

        // 스택 리스트 교체
        if (newStacks != null && !newStacks.isEmpty()) {
            user.getStackList().clear();
            for (UserRequest.InfoRequest.Stack stackDto : newStacks) {
                Stack stack = new Stack();
                stack.setStackName(stackDto.getStackName());
                stack.setUser(user);
                user.getStackList().add(stack);
            }
        }

        // 사용자 정보 수정 dto 생성
        List<UserResponse.Info.Stack> stackDTOs = new ArrayList<>();
        for (Stack stack : user.getStackList()) {
            UserResponse.Info.Stack dto = UserResponse.Info.Stack.builder()
                    .stackName(stack.getStackName())
                    .build();
            stackDTOs.add(dto);
        }

        userRepository.save(user);

        // dto 반환
        return UserResponse.Info.builder()
                .name(user.getName())
                .position(user.getPosition())
                .seniority(user.getSeniority())
                .stacks(stackDTOs)
                .build();
    }

}
