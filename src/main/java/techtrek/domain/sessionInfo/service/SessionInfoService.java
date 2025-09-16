package techtrek.domain.sessionInfo.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.sessionInfo.dto.SessionInfoRequest;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.service.component.*;
import techtrek.global.securty.service.CustomUserDetails;

@RequiredArgsConstructor
@Builder
@Service
public class SessionInfoService {

    private final CreateStartInterview createStartInterview;
    private final CreateBasicInterview createNewInterview;
    private final CreateTailInterview createTailInterview;
    private final CreateAnswer createAnswer;
    private final DeleteInterview deleteInterview;

    //면접 시작하기
    public SessionInfoResponse.Start createInterview(SessionInfoRequest.Start request, CustomUserDetails userDetails) {
        return createStartInterview.exec(request.getEnterpriseName(),userDetails);
    }

    //새로운 질문 생성하기
    public SessionInfoResponse.NewQuestion createNewInterview(SessionInfoRequest.NewQuestion request, CustomUserDetails userDetails) {
        return createNewInterview.exec(request.getSessionId(), request.getPreviousId(),userDetails);
    }

    // 꼬리 질문 생성하기
    public SessionInfoResponse.TailQuestion createTailInterview(SessionInfoRequest.TailQuestion request, CustomUserDetails userDetails) {
       return createTailInterview.exec(request.getSessionId(),request.getParentId(),request.getPreviousId(),userDetails);
    }

    //답변하기
    public Boolean createAnswer(SessionInfoRequest.Answer request, CustomUserDetails userDetails) {
        return createAnswer.exec(request. getSessionId(),request.getFieldId(),request.getType(),request.getAnswer(),userDetails);
    }

    // 종료하기
    public Boolean deleteInterview(String sessionInfoId, CustomUserDetails userDetails){
        return deleteInterview.exec(sessionInfoId, userDetails);
    }
}


