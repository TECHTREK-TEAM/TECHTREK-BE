package techtrek.domain.Interview.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.Interview.dto.InterviewRequest;
import techtrek.domain.Interview.dto.InterviewResponse;
import techtrek.domain.Interview.service.component.*;

@RequiredArgsConstructor
@Builder
@Service
public class InterviewService {
    private final CreateStartInterview createStartInterview;
    private final CreateBasicInterview createBasicInterview;
    private final CreateResumeInterview createResumeInterview;
    private final CreateAnswer createAnswerBean;
    private final DeleteInterview deleteInterview;
    private final CreateParentTailInterview createParentTailInterview;
    private final CreatePreviousTailInterview createPreviousTailInterview;

    //면접 시작하기
    public InterviewResponse.Start createInterview(InterviewRequest.Start request) {
        return createStartInterview.exec(request.getEnterpriseName());
    }

    // 기본 질문 생성하기
    public InterviewResponse.Question createNewInterview(InterviewRequest.Question request) {
        return createBasicInterview.exec(request.getSessionId());
    }

    // 이력서 질문 생성하기
    public InterviewResponse.Question createResumeInterview(InterviewRequest.Question request) {
        return createResumeInterview.exec(request.getSessionId());
    }

    // 연계 질문 생성하기
    public InterviewResponse.TailQuestion createTailInterview(InterviewRequest.TailQuestion request) {
        // 첫번째 연계질문
        if (request.getPreviousId() == null || request.getPreviousId().isBlank()) return createParentTailInterview.exec(request.getSessionId(), request.getParentId());
        // 그 이후 연계질문
        else return createPreviousTailInterview.exec(request.getSessionId(), request.getPreviousId());
    }

    //답변하기
    public Boolean createAnswer(InterviewRequest.Answer request) {
        return createAnswerBean.exec(request. getSessionId(),request.getFieldId(),request.getType(),request.getAnswer());
    }

    // 종료하기
    public Boolean deleteInterview(String sessionId){
        return deleteInterview.exec(sessionId);
    }
}


