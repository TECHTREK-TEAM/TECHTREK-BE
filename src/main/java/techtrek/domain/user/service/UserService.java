package techtrek.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.UserRequest;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.service.component.*;
import techtrek.global.securty.service.CustomUserDetails;

@Service
@RequiredArgsConstructor
public class UserService {
    private final GetUser getUser;
    private final UpdateUser updateUser;
    private final GetCompany getCompany;
    private final GetPass getPass;
    private final GetScore getScore;
    private final GetInterview getInterview;
    private final CreateResume createResume;

    // 사용자 정보 조회
    public UserResponse.Info getUser(CustomUserDetails userDetails){
        return getUser.exec(userDetails);
    }

    // 사용자 정보 수정
    public UserResponse.Info updateUser(UserRequest.Info request,CustomUserDetails userDetails){
        return updateUser.exec(request.getName(), request.getPosition(), request.getSeniority(), request.getStacks(), userDetails);
    }

    // 관심 기업 조회
    public UserResponse.CompanyList getCompany(CustomUserDetails userDetails){
        return getCompany.exec(userDetails);
    }

    // 합격률 조회
    public UserResponse.Pass getPass(CustomUserDetails userDetails){
        return getPass.exec(userDetails);
    }

    // 일치율 조회
    public UserResponse.Score getScore(CustomUserDetails userDetails){
        return getScore.exec(userDetails);
    }

    // 면접 정보 조회
    public UserResponse.Interview getInterview(CustomUserDetails userDetails){
        return getInterview.exec(userDetails);
    }

    // 이력서 생성
    public UserResponse.Resume createResume(MultipartFile file, CustomUserDetails userDetails){
        return createResume.exec(file,userDetails);
    }
}
