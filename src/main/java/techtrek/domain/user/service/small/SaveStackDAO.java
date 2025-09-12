//package techtrek.domain.user.service.small;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import techtrek.domain.stack.entity.Stack;
//import techtrek.domain.stack.repository.StackRepository;
//import techtrek.domain.user.dto.UserResponse;
//import techtrek.domain.user.entity.User;
//
//import java.util.List;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class SaveStackDAO {
//
//    private final StackRepository stackRepository;
//
//    // 스택 저장
//    public void exec(User user, List<UserResponse.Resume.Stack> stacks) {
//        // 기존 스택 삭제
//        stackRepository.deleteByUserId(user.getId());
//
//        // 새로운 스택 리스트 생성 (builder 사용)
//        List<Stack> newStacks = stacks.stream()
//                .map(dto -> Stack.builder()
//                        .id(UUID.randomUUID().toString())
//                        .stackName(dto.getStackName())
//                        .user(user)
//                        .build())
//                .toList();
//
//        stackRepository.saveAll(newStacks);
//    }
//}
