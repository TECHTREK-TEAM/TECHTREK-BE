package techtrek.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.UserRequest;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.service.bean.*;
import techtrek.global.securty.service.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class UserService {
    private final GetUserBean getUserBean;
    private final UpdateUserBean updateUserBean;
    private final GetCompanyBean getCompanyBean;
    private final GetPassBean getPassBean;
    private final GetScoreBean getScoreBean;
    private final GetInterviewBean getInterviewBean;
    private final CreateResumeBean createResumeBean;

    // 사용자 정보 조회
    public UserResponse.Info getUser(CustomUserDetails userDetails){
        return getUserBean.exec(userDetails);
    }

    // 사용자 정보 수정
    public UserResponse.Info updateUser(UserRequest.Info request,CustomUserDetails userDetails){
        return updateUserBean.exec(request.getName(), request.getUserGroup(), request.getSeniority(), request.getStacks(),userDetails);
    }

    // 관심 기업 조회
    public UserResponse.CompanyList getCompany(CustomUserDetails userDetails){
        return getCompanyBean.exec(userDetails);
    }

    // 합격률 조회
    public UserResponse.Pass getPass(CustomUserDetails userDetails){
        return getPassBean.exec(userDetails);
    }

    // 일치율 조회
    public UserResponse.Score getScore(CustomUserDetails userDetails){
        return getScoreBean.exec(userDetails);
    }

    // 면접 정보 조회
    public UserResponse.Interview getInterview(CustomUserDetails userDetails){
        return getInterviewBean.exec(userDetails);
    }

    // 이력서 생성
    public UserResponse.Resume createResume(MultipartFile file, CustomUserDetails userDetails){ return createResumeBean.exec(file,userDetails); }
}
