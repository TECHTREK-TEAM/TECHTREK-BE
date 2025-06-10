package techtrek.domain.user.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.user.dto.UserRequest;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UpdateUserDAOBean {
    private final UserRepository userRepository;

    // 사용자 정보 수정 저장
    public void exec(User user, String newName, String newSeniority, String newUserGroup, List<UserRequest.Stack> newStacks){
        if (newName != null) user.setName(newName);
        if (newUserGroup != null) user.setUserGroup(newUserGroup);
        if (newSeniority != null) user.setSeniority(newSeniority);

        // 사용자 스택 수정
        if (newStacks != null) {
            // 기존 스택 삭제
            List<Stack> currentStacks = user.getStackList();
            currentStacks.clear();

            // 새 스택 리스트 저장
            for (UserRequest.Stack dto : newStacks) {
                Stack stack = new Stack();
                stack.setId(UUID.randomUUID().toString());
                stack.setStackName(dto.getStackName());
                stack.setUser(user);
                currentStacks.add(stack);
            }
        }

        userRepository.save(user);
    }
}
