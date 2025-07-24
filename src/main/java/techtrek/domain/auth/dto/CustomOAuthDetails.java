package techtrek.domain.auth.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CustomOAuthDetails implements OAuth2User {
    private final String id;
    private final String name;
    private final String email;

    private final Collection<? extends GrantedAuthority> authorities;

    public CustomOAuthDetails(String id, String name, String email) {
        this(id, name, email, Collections.emptyList());
    }

    @Override
    @JsonIgnore
    public Map<String, Object> getAttributes() {
        return Map.of(
                "id", id,
                "name", name,
                "email", email
        );
    }
}


