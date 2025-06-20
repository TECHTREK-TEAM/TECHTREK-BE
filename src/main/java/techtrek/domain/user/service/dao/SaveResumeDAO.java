package techtrek.domain.user.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class SaveResumeDAO {

    private final UserRepository userRepository;

    // 이력서 및 직군, 연차 저장
    public void exec(User user, String group, String seniority, String resume) {
        user.setUserGroup(group);
        user.setSeniority(seniority);
        user.setResume(resume);

        userRepository.save(user);
    }
}

