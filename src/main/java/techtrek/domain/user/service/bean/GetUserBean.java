package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dto.GetUserDTO;
import techtrek.domain.user.service.dao.GetUserDAO;

@Component
@RequiredArgsConstructor
public class GetUserBean {

    private final GetUserDAO getUserDAO;
    private final GetUserDTO getUserDTO;

    public UserResponse.Info exec() {
        // 사용자 조회
        User user = getUserDAO.exec("1");

        return getUserDTO.exec(user);
    }
}
