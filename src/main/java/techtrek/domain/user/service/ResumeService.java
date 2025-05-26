package techtrek.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import techtrek.domain.user.dto.ResumeResponse;
import techtrek.domain.user.service.bean.CreateResumeBean;

@Service
@RequiredArgsConstructor
public class ResumeService {

    private final CreateResumeBean createResumeBean;

    // 이력서 추출
    public ResumeResponse createResume(MultipartFile file){
        return createResumeBean.exec(file);

    }
}

