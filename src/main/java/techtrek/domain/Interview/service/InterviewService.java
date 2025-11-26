package techtrek.domain.Interview.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.Interview.dto.InterviewRequest;
import techtrek.domain.Interview.dto.InterviewResponse;
import techtrek.domain.Interview.service.component.*;
import techtrek.global.securty.service.CustomUserDetails;

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
    public InterviewResponse.Start createInterview(InterviewRequest.StartRequest request, CustomUserDetails userDetails) {
        return createStartInterview.exec(request.getEnterpriseName(), userDetails);
    }

    // 기본 질문 생성하기
    public InterviewResponse.Question createNewInterview(InterviewRequest.QuestionRequest request, CustomUserDetails userDetails) {
        return createBasicInterview.exec(request.getSessionId(), userDetails);
    }

    // 이력서 질문 생성하기
    public InterviewResponse.Question createResumeInterview(InterviewRequest.QuestionRequest request, CustomUserDetails userDetails) {
        return createResumeInterview.exec(request.getSessionId(), userDetails);
    }

    // 연계 질문 생성하기
    public InterviewResponse.TailQuestion createTailInterview(InterviewRequest.TailQuestionRequest request, CustomUserDetails userDetails) {
        // 첫번째 연계질문
        if (request.getPreviousId() == null || request.getPreviousId().isBlank()) return createParentTailInterview.exec(request.getSessionId(), request.getParentId(), userDetails);
        // 그 이후 연계질문
        else return createPreviousTailInterview.exec(request.getSessionId(), request.getPreviousId(), userDetails);
    }

    //답변하기
    public Boolean createAnswer(InterviewRequest.AnswerRequest request, CustomUserDetails userDetails) {
        return createAnswerBean.exec(request. getSessionId(),request.getFieldId(),request.getType(),request.getAnswer(),userDetails);
    }

    // 종료하기
    public Boolean deleteInterview(String sessionId, CustomUserDetails userDetails){
        return deleteInterview.exec(sessionId, userDetails);
    }
}


