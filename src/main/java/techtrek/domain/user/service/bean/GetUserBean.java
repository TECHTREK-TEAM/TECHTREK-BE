package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.CreateUserInfoDTOBean;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;

@Component
@RequiredArgsConstructor
public class GetUserBean {

    private final GetUserDAOBean getUserDAOBean;
    private final CreateUserInfoDTOBean createUserInfoDTOBean;

    public UserResponse.Info exec() {
        // 사용자 조회
        User user = getUserDAOBean.exec("1");

        return createUserInfoDTOBean.exec(user);
    }
}
