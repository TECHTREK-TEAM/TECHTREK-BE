package techtrek.domain.interview.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.interview.dto.SessionInfoRequest;
import techtrek.domain.interview.dto.SessionInfoResponse;
import techtrek.domain.interview.service.component.*;

@RequiredArgsConstructor
@Builder
@Service
public class SessionInfoService {

    private final CreateStartInterview createStartInterviewBean;
    private final CreateNewInterviewBean createNewInterviewBean;
    private final CreateTailInterviewBean createTailInterviewBean;
    private final CreateAnswerBean createAnswerBean;
    private final DeleteInterview deleteInterview;

    //면접 시작하기
    public SessionInfoResponse.Start createInterview() {
        return createStartInterviewBean.exec();
    }

    //새로운 질문 생성하기
    public SessionInfoResponse.NewQuestion createNewInterview(SessionInfoRequest.NewQuestion request) {
        return createNewInterviewBean.exec(request.getSessionId(), request.getPreviousId());
    }

    // 꼬리 질문 생성하기
    public SessionInfoResponse.TailQuestion createTailInterview(SessionInfoRequest.TailQuestion request) {
       return createTailInterviewBean.exec(request.getSessionId(),request.getParentId(),request.getPreviousId());
    }

    //답변하기
    public Boolean createAnswer(SessionInfoRequest.Answer request) {
        return createAnswerBean.exec(request. getSessionId(),request.getFieldId(),request.getType(),request.getAnswer());
    }

    // 종료하기
    public Boolean deleteInterview(String sessionInfoId){
        return deleteInterview.exec(sessionInfoId);
    }
}


