package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.global.redis.service.bean.small.SaveAnswerDAOBean;

@Component
@RequiredArgsConstructor
public class CreateAnswerBean {
    private final SaveAnswerDAOBean saveAnswerDAOBean;

    // 답변하기
    public Boolean exec(String sessionId, String fieldId, String type, String answer){
        // 세션 키
        String fieldKey = "interview:session:" + sessionId + ":" + type + ":"+ fieldId;

        // 답변 저장
        saveAnswerDAOBean.exec(fieldKey,answer);

        return true;
    };
}
