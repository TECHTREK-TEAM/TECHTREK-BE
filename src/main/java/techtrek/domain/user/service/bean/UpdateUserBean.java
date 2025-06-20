package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.dto.UserRequest;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dao.GetUserDAO;
import techtrek.domain.user.service.dto.GetUserDTO;
import techtrek.domain.user.service.dao.UpdateUserDAO;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateUserBean {
    private final GetUserDAO getUserDAO;
    private final UpdateUserDAO updateUserDAO;
    private final GetUserDTO getUserDTO;

    // 사용자 정보 수정
    public UserResponse.Info exec (String newName, String newUserGroup, String newSeniority, List<UserRequest.Stack> newStacks) {
        // 사용자 조회
        User user = getUserDAO.exec("1");

        // 사용자 정보 수정 저장
        updateUserDAO.exec(user, newName, newUserGroup, newSeniority, newStacks);

        // 사용자 정보 수정 dto
        return getUserDTO.exec(user);

    }
}
