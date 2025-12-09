package techtrek.global.securty.service;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import techtrek.domain.user.entity.Role;
import techtrek.domain.user.entity.User;

import java.util.Collection;
import java.util.List;

@Getter
public class CustomUserDetails implements UserDetails {

    private final String id;
    private final String email;
    private final String name;
    private final Role role;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.name = user.getName();
        this.role = user.getRole();
        // 권한은 ROLE_ 접두어 없이 바로 등록
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }

    // 사용자의 이메일을 username으로 사용
    @Override public String getUsername() { return email; }

    // 소셜 로그인 등 비밀번호가 필요 없을 경우 null 반환 가능
    @Override public String getPassword() { return null; }

    // 계정 상태 설정 (true로 설정 시 활성 계정)
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
