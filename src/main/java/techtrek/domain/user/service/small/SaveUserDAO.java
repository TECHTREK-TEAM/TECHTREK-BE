package techtrek.domain.user.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class SaveUserDAO {

    private final UserRepository userRepository;

    // 신규 사용자 생성 + 저장
    public User exec(String provider, String name, String email, String accessToken) {
        User user = User.builder()
                .id(provider + "_" + accessToken)
                .name(name)
                .email(email)
                .provider(provider)
                .createdAt(LocalDateTime.now().withNano(0))
                .build();

        return userRepository.save(user);
    }
}
