package techtrek.domain.user.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class GetUserByEmailDAO {

    private final UserRepository userRepository;

    // 이메일로 사용자 조회
    public User exec(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}

