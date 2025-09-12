//package techtrek.domain.user.service.small;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import techtrek.domain.stack.entity.Stack;
//import techtrek.domain.user.dto.UserRequest;
//import techtrek.domain.user.entity.User;
//import techtrek.domain.user.repository.UserRepository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//public class UpdateUserDAO {
//    private final UserRepository userRepository;
//
//    // 사용자 정보 수정
//    public void exec(User user, String newName, String newSeniority, String newRole, List<UserRequest.Info.Stack> newStacks){
//        // 이름, 그룹, 시니어리티 변경
//        if (newName != null) user.changeUsername(newName);
//        if (newRole != null) user.changeRole(newRole);
//        if (newSeniority != null) user.changeSeniority(newSeniority);
//
//        // 타임스탬프 갱신
//        user.updateTimestamp();
//
//        // 스택 리스트 교체
//        if (newStacks != null) {
//            List<Stack> newStackList = newStacks.stream()
//                    .map(dto -> Stack.builder()
//                            .id(UUID.randomUUID().toString())
//                            .stackName(dto.getStackName())
//                            .user(user)
//                            .build())
//                    .toList();
//
//            user.replaceStacks(newStackList);
//        }
//
//        userRepository.save(user);
//    }
//}
