//package techtrek.domain.user.service.small;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import techtrek.domain.user.entity.User;
//import techtrek.domain.user.repository.UserRepository;
//
//@Component
//@RequiredArgsConstructor
//public class SaveResumeDAO {
//
//    private final UserRepository userRepository;
//
//    // 이력서 및 직군, 연차 저장
//    public void exec(User user, String role, String seniority, String resume) {
//        if (role != null) user.changeRole(role);
//        if (seniority != null) user.changeSeniority(seniority);
//        if (resume != null) user.changeResume(resume);
//
//        userRepository.save(user);
//    }
//}
//
