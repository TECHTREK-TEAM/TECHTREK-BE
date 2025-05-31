package techtrek.domain.user.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class GetUserDAOBean {

    private final UserRepository userRepository;

    // 사용자 조회
    public User exec(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return user;
    }
}
