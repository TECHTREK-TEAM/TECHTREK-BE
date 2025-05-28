package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.service.bean.helper.CreateAnswerHelper;

@Component
@RequiredArgsConstructor
public class CreateAnswerBean {
    private final CreateAnswerHelper createAnswerHelper;

    public Boolean exec(String sessionId, String fieldId, String type, String answer){
        // 세션 키
        String sessionKey = "interview:session:" + sessionId + ":" + type;

        // 답변
        return createAnswerHelper.exec(sessionKey,fieldId, answer);
    };
}
