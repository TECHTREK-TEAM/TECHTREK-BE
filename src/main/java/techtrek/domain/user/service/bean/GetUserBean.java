package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.CreateUserDTO;
import techtrek.domain.user.service.small.GetUserDAO;

@Component
@RequiredArgsConstructor
public class GetUserBean {

    private final GetUserDAO getUserDAO;
    private final CreateUserDTO createUserDTO;

    public UserResponse.Info exec() {
        // TODO:사용자 조회
        User user = getUserDAO.exec("1");

        return createUserDTO.exec(user);
    }
}
