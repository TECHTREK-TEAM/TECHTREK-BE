package techtrek.domain.sessionInfo.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.service.bean.CreateAnswerBean;
import techtrek.domain.sessionInfo.service.bean.CreateStartInterviewBean;
import techtrek.domain.sessionInfo.service.bean.CreateNewInterviewBean;
import techtrek.domain.sessionInfo.service.bean.CreateTailInterviewBean;

@RequiredArgsConstructor
@Builder
@Service
public class SessionInfoService {

    private final CreateStartInterviewBean createStartInterviewBean;
    private final CreateNewInterviewBean createNewInterviewBean;
    private final CreateTailInterviewBean createTailInterviewBean;
    private final CreateAnswerBean createAnswerBean;

    //면접 시작하기
    public SessionInfoResponse.Start createInterview(String enterpriseName, String enterpriseType) {
        return createStartInterviewBean.exec(enterpriseName, enterpriseType);
    }

    //새로운 질문 생성하기
    public SessionInfoResponse.NewQuestion createNewInterview(String sessionId, String previousFieldId) {
        return createNewInterviewBean.exec(sessionId, previousFieldId);
    }

    // 꼬리 질문 생성하기
    public SessionInfoResponse.TailQuestion createTailInterview(String sessionId, String parentId, String previousFieldId) {
       return createTailInterviewBean.exec(sessionId,parentId,previousFieldId);
    }

    //답변하기
    public Boolean createAnswer(String sessionId, String fieldId, String type, String answer) {
        return createAnswerBean.exec(sessionId,fieldId,type,answer);
    }

}


