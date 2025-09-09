package techtrek.domain.session.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.session.dto.SessionRequest;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.service.component.*;

@RequiredArgsConstructor
@Builder
@Service
public class SessionService {

    private final CreateStartInterview createStartInterviewBean;
    private final CreateNewInterviewBean createNewInterviewBean;
    private final CreateTailInterviewBean createTailInterviewBean;
    private final CreateAnswerBean createAnswerBean;
    private final DeleteInterview deleteInterview;

    //면접 시작하기
    public SessionResponse.Start createInterview(SessionRequest.Start request) {
        return createStartInterviewBean.exec(request.getEnterpriseName());
    }

    //새로운 질문 생성하기
    public SessionResponse.NewQuestion createNewInterview(SessionRequest.NewQuestion request) {
        return createNewInterviewBean.exec(request.getSessionId(), request.getPreviousId());
    }

    // 꼬리 질문 생성하기
    public SessionResponse.TailQuestion createTailInterview(SessionRequest.TailQuestion request) {
       return createTailInterviewBean.exec(request.getSessionId(),request.getParentId(),request.getPreviousId());
    }

    //답변하기
    public Boolean createAnswer(SessionRequest.Answer request) {
        return createAnswerBean.exec(request. getSessionId(),request.getFieldId(),request.getType(),request.getAnswer());
    }

    // 종료하기
    public Boolean deleteInterview(String sessionInfoId){
        return deleteInterview.exec(sessionInfoId);
    }
}


