package techtrek.global.securty.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.GetUserDAO;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final GetUserDAO getUserDAO;

    // JWT 토큰에서 추출한 userId로 사용자 조회
    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = getUserDAO.exec(userId);

        // CustomUserDetails는 스프링 시큐리티에서 사용자 정보를 담는 객체
        return new CustomUserDetails(user);
    }
}

