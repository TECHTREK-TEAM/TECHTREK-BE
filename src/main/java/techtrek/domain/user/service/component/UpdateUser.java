package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.dto.UserRequest;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.GetUserDAO;
import techtrek.domain.user.service.small.CreateUserDTO;
import techtrek.domain.user.service.small.UpdateUserDAO;
import techtrek.global.securty.service.CustomUserDetails;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateUser {
    private final GetUserDAO getUserDAO;
    private final UpdateUserDAO updateUserDAO;
    private final CreateUserDTO createUserDTO;

    // 사용자 정보 수정
    public UserResponse.Info exec (String newName, String newUserGroup, String newSeniority, List<UserRequest.Info.Stack> newStacks, CustomUserDetails userDetails) {
        // 사용자 조회
        User user = getUserDAO.exec(userDetails.getId());

        // 사용자 정보 수정 저장
        updateUserDAO.exec(user, newName, newUserGroup, newSeniority, newStacks);

        // 사용자 정보 수정 dto
        return createUserDTO.exec(user);

    }
}
