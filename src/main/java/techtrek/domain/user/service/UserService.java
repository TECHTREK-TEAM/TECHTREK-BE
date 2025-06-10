package techtrek.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.user.dto.UserRequest;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.service.bean.GetCompanyBean;
import techtrek.domain.user.service.bean.GetUserBean;
import techtrek.domain.user.service.bean.UpdateUserBean;

@Service
@RequiredArgsConstructor
public class UserService {
    private final GetUserBean getUserBean;
    private final UpdateUserBean updateUserBean;
    private final GetCompanyBean getCompanyBean;

    // 사용자 정보 조회
    public UserResponse.Info getUser(){
        return getUserBean.exec();
    }

    // 사용자 정보 수정
    public UserResponse.Info updateUser(UserRequest.Info request){
        return updateUserBean.exec(request.getName(), request.getUserGroup(), request.getSeniority(), request.getStacks());
    }

    // 관심 기업 조회
    public UserResponse.CompanyList getCompany(){
        return getCompanyBean.exec();
    }
}
