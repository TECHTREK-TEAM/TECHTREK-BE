package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.user.dto.UserRequest;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.domain.user.service.bean.small.GetUserDTOBean;
import techtrek.domain.user.service.bean.small.UpdateUserDAOBean;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateUserBean {
    private final GetUserDAOBean getUserDAOBean;
    private final UpdateUserDAOBean updateUserDAOBean;
    private final GetUserDTOBean getUserDTOBean;

    // 사용자 정보 수정
    public UserResponse.Info exec (String newName, String newUserGroup, String newSeniority, List<UserRequest.Stack> newStacks) {
        // 사용자 조회
        User user = getUserDAOBean.exec("1");

        // 사용자 정보 수정 저장
        updateUserDAOBean.exec(user, newName, newUserGroup, newSeniority, newStacks);

        // 사용자 정보 수정 dto
        return getUserDTOBean.exec(user);

    }
}
