package techtrek.global.securty.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import techtrek.global.securty.filter.JwtAuthenticationFilter;
import techtrek.global.securty.provider.JwtProvider;
import techtrek.global.securty.service.CustomUserDetailsService;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final RestAuthEntryPoint restAuthEntryPoint;
    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(httpBasic -> httpBasic.disable()) // httpBasic 인증 비활성화
                .csrf(csrf -> csrf.disable())                // CSRF 비활성화 (API 서버용)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )                                             // 세션 미사용 (JWT 기반)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() // Preflight 요청 허용
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/v3/api-docs/**",
                                "/api-docs/**"
                        ).permitAll()
                        .requestMatchers("/api/interview/questions/tail").permitAll()
                        .requestMatchers("/oauth2/**","/auth/**").permitAll()  // 인증 없이 접근 허용
                        .anyRequest().authenticated()                            // 나머지 요청 인증 필요
                )
                .exceptionHandling(exception -> exception // rest api용 인증 실패 처리
                        .authenticationEntryPoint(restAuthEntryPoint)
                )
                .oauth2Login(oauth -> oauth.loginPage("/login"))    // oauth2 인증 실패 처리
                .addFilterBefore(
                        new JwtAuthenticationFilter(jwtProvider, userDetailsService),
                        UsernamePasswordAuthenticationFilter.class
                )
                .cors(withDefaults()); // CORS 설정 활성화

        return http.build();
    }

    // CORS 설정 직접 등록
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5173");
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH","OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}