//package techtrek.global.config;
//
//import jakarta.annotation.PostConstruct;
//import lombok.RequiredArgsConstructor;
//import org.springframework.context.annotation.Configuration;
//import techtrek.domain.user.entity.User;
//import techtrek.domain.user.repository.UserRepository;
//
//import java.time.LocalDateTime;
//
//// TODO: 로그인,회원가입 구현 후 삭제
//@Configuration
//@RequiredArgsConstructor
//public class UserInitConfig {
//
//    private final UserRepository userRepository;
//
//    @PostConstruct
//    public void init() {
//        if (userRepository.existsById("1")) return;
//
//        User user = User.builder()
//                .id("1")
//                .name("홍길동")
//                .userGroup("Frontend Developer")
//                .seniority("지망생")
//                .resume("Java와 Spring Boot 기반 백엔드 개발에 집중해 JWT 인증, S3 이미지 업로드, 예산 CRUD API 등을 구현했으며, JPA 사용 시 발생하는 N+1 문제 해결과 쿼리 튜닝, Lazy Loading을 통한 성능 최적화 경험이 있습니다.")
//                .createdAt(LocalDateTime.now().withNano(0))
//                .build();
//
//        userRepository.save(user);
//    }
//}
