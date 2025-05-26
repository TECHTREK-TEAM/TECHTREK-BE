package techtrek.domain.user.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.stack.repository.StackRepository;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SaveStackDAOBean {

    private final UserRepository userRepository;
    private final StackRepository stackRepository;

    // 스택 저장
    public void exec(String userId, List<ResumeResponse.Stack> stacks) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Stack> newStacks = new ArrayList<>();
        for (ResumeResponse.Stack dto : stacks) {
            Stack stack = new Stack();
            stack.setId(UUID.randomUUID().toString());
            stack.setStackName(dto.getStackName());
            stack.setUser(user);
            newStacks.add(stack);
        }

        stackRepository.saveAll(newStacks);
    }
}
