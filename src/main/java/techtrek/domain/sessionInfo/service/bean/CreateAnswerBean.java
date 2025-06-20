package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.redis.service.dao.SaveAnswerDAO;

@Component
@RequiredArgsConstructor
public class CreateAnswerBean {
    private final SaveAnswerDAO saveAnswerDAO;

    // 답변하기
    public Boolean exec(String sessionId, String fieldId, String type, String answer){
        // 세션 키
        String fieldKey = "interview:session:" + sessionId + ":" + type + ":"+ fieldId;

        // 답변 저장
        saveAnswerDAO.exec(fieldKey,answer);

        return true;
    };
}
