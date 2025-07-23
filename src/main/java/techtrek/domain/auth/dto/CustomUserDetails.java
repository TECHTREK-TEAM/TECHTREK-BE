package techtrek.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomUserDetails implements UserDetails {

    private final String userId;
    private final String name;
    private final String email;
    private final String provider;
    private final Collection<? extends GrantedAuthority> authorities;

    @JsonCreator
    public CustomUserDetails(
            @JsonProperty("userId") String userId,
            @JsonProperty("name") String name,
            @JsonProperty("email") String email,
            @JsonProperty("provider") String provider,
            @JsonProperty("authorities") Collection<? extends GrantedAuthority> authorities
    ) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.provider = provider;
        this.authorities = authorities;
    }

    // Spring Security 필수 구현 메서드
    @Override
    public String getUsername() {
        return this.userId; // 소셜 식별자
    }

    @Override
    public String getPassword() {
        return null; // 소셜 로그인에서는 비밀번호 없음
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
