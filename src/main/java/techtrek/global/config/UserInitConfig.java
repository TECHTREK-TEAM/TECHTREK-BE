package techtrek.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import techtrek.domain.stack.entity.Stack;
import techtrek.domain.user.entity.Role;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// TODO: 로그인,회원가입 구현 후 삭제
@Profile("!test")
@Configuration
@RequiredArgsConstructor
public class UserInitConfig {

    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        if (userRepository.count() > 0) return;

        User user = User.builder()
                .id("1")
                .name("홍길동")
                .position("Frontend Developer")
                .email("user01@naver.com")
                .provider("kakao")
                .seniority("지망생")
                .resume("Java와 Spring Boot...")
                .resumeName("예시 이력서.pdf")
                .role(Role.USER)
                .createdAt(LocalDateTime.now().withNano(0))
                .build();

        Stack stack = Stack.builder()
                .stackName("react")
                .user(user)      // user 넣어줌
                .build();

        List<Stack> stackList = new ArrayList<>();
        stackList.add(stack);

        user.setStackList(stackList); // User 엔티티에 setter로 바로 넣기

        userRepository.save(user);
    }
}
