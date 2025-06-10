package techtrek.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.service.bean.GetUserBean;

@Service
@RequiredArgsConstructor
public class UserService {
    private final GetUserBean getUserBean;

    // 사용자 불러오기
    public UserResponse.Info getUser(){
        return getUserBean.exec();

    }
}
