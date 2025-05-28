package techtrek.domain.user.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.stack.repository.StackRepository;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.domain.user.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SaveStackDAOBean {

    private final StackRepository stackRepository;

    // 스택 저장
    public void exec(User user, List<ResumeResponse.Stack> stacks) {
        // 기존 스택 삭제
        stackRepository.deleteByUserId(user.getId());

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
