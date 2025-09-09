package techtrek.domain.Interview.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.Interview.dto.SessionRequest;
import techtrek.domain.Interview.dto.SessionResponse;
import techtrek.domain.Interview.service.component.*;

@RequiredArgsConstructor
@Builder
@Service
public class InterviewService {

    private final CreateStartInterview createStartInterview;
    private final CreateBasicInterview createBasicInterview;
    private final CreateResumeInterview createResumeInterview;
    private final CreateTailInterviewBean createTailInterviewBean;
    private final CreateAnswerBean createAnswerBean;
    private final DeleteInterview deleteInterview;

    //면접 시작하기
    public SessionResponse.Start createInterview(SessionRequest.Start request) {
        return createStartInterview.exec(request.getEnterpriseName());
    }

    // 기본 질문 생성하기
    public SessionResponse.Question createNewInterview(SessionRequest.Question request) {
        return createBasicInterview.exec(request.getSessionId());
    }

    // 이력서 질문 생성하기
    public SessionResponse.Question createResumeInterview(SessionRequest.Question request) {
        return createResumeInterview.exec(request.getSessionId());
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


