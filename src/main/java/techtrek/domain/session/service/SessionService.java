package techtrek.domain.session.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.session.dto.SessionRequest;
import techtrek.domain.session.dto.SessionResponse;
import techtrek.domain.session.service.component.*;
import techtrek.global.securty.service.CustomUserDetails;

@RequiredArgsConstructor
@Builder
@Service
public class SessionService {
    private final CreateStartInterview createStartInterview;
    private final CreateBasicInterview createBasicInterview;
    private final CreateResumeInterview createResumeInterview;
    private final CreateAnswer createAnswerBean;
    private final DeleteInterview deleteInterview;
    private final CreateTailInterview createTailInterview;

    //면접 시작하기
    public SessionResponse.Start createInterview(SessionRequest.StartRequest request, CustomUserDetails userDetails) {
        return createStartInterview.exec(request.getEnterpriseName(), userDetails);
    }

    // 기본 질문 생성하기
    public SessionResponse.Question createNewInterview(SessionRequest.QuestionRequest request, CustomUserDetails userDetails) {
        return createBasicInterview.exec(request.getSessionId(), userDetails);
    }

    // 이력서 질문 생성하기
    public SessionResponse.Question createResumeInterview(SessionRequest.QuestionRequest request, CustomUserDetails userDetails) {
        return createResumeInterview.exec(request.getSessionId(), userDetails);
    }

    // 연계 질문 생성하기
    public SessionResponse.TailQuestion createTailInterview(SessionRequest.TailQuestionRequest request, CustomUserDetails userDetails) {
        return createTailInterview.exec(request.getSessionId(), userDetails);
    }

    //답변하기
    public Boolean createAnswer(SessionRequest.AnswerRequest request, CustomUserDetails userDetails) {
        return createAnswerBean.exec(request. getSessionId(),request.getAnswer(),userDetails);
    }

    // 종료하기
    public Boolean deleteInterview(String sessionId, CustomUserDetails userDetails){
        return deleteInterview.exec(sessionId, userDetails);
    }
}


