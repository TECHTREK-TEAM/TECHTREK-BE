package techtrek.global.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import techtrek.domain.stack.entity.Stack;
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
        if (userRepository.existsById("1")) return;

        User user = User.builder()
                .id("1")
                .name("홍길동")
                .email("user01@naver.com")
                .provider("kakao")
                .userGroup("Frontend Developer")
                .seniority("지망생")
                .resume("Java와 Spring Boot...")
                .createdAt(LocalDateTime.now().withNano(0))
                .build();

        Stack stack = Stack.builder()
                .id("1")
                .stackName("react")
                .user(user)      // user 넣어줌
                .build();

        // user에 stackList 넣은 새 user 생성 (toBuilder)
        user = user.toBuilder()
                .stackList(new ArrayList<>(List.of(stack)))  // mutable 리스트로 꼭 감싸기
                .build();

        userRepository.save(user);
    }
}
