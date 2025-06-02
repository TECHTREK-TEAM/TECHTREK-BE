package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisResponse;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.bean.manager.CreateBasicManager;
import techtrek.domain.sessionInfo.service.bean.manager.CreateResumeManager;
import techtrek.domain.sessionInfo.service.bean.small.*;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.global.redis.service.bean.small.GetRedisPreviousDAOBean;
import techtrek.global.redis.service.bean.small.SaveNewQuestionDAOBean;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateNewInterviewBean {
    private final CreateBasicManager createBasicManager;
    private final CreateResumeManager createResumeManager;

    private final GetUserDAOBean getUserDAOBean;
    private final GetSessionInfoDAOBean getSessionInfoDAOBean;
    private final CheckSessionInfoDAOBean checkSessionInfoDAOBean;
    private final GetRedisPreviousDAOBean getRedisPreviousDAOBean;
    private final SaveNewQuestionDAOBean saveNewQuestionDAOBean;

    private final SaveSessionInfoDTOBean saveSessionInfoDTOBean;

    // 새로운 질문 생성하기 ( 기본질문 or 이력서기반)
    public SessionInfoResponse.NewQuestion exec(String sessionId, String previousFieldId){
        // 세션 존재 확인
        checkSessionInfoDAOBean.exec(sessionId);

        // 사용자, 기업이름, 기업유형 조회
        User user = getUserDAOBean.exec("1");
        SessionInfo sessionInfo = getSessionInfoDAOBean.exec(sessionId);
        EnterpriseName enterpriseName = sessionInfo.getEnterpriseName();

        // 필드Id, 세션키 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = "interview:session:" + sessionId;
        String previousKey = sessionKey + ":new:" + previousFieldId;
        String newkey = sessionKey +":new";
        String fieldKey = newkey+":" +fieldId;
        String question = "";

        // 이전 질문 조회
        RedisResponse.PreviousData lastQuestion = getRedisPreviousDAOBean.exec(previousKey);
        String phase = lastQuestion.getPhase();
        int questionNumber = Integer.parseInt(lastQuestion.getQuestionNumber());
        int totalQuestionNumber = Integer.parseInt(lastQuestion.getTotalQuestionNumber());
        int count = Integer.parseInt(lastQuestion.getCount());

        // 만약 count가 5이상이라면, 기본질문 <-> 이력서 기반 질문 변경
        if (count >= 5) {
            phase = phase.equals("basic") ? "resume" : "basic";
            count = 0;
        }

        // phase가 basic일 경우
        if (phase.equals("basic")) question = createBasicManager.exec(enterpriseName);
        // phase가 resume일 경우
        else question = createResumeManager.exec(user, sessionId);

        // 총 질문 번호, 질문 번호
        String resultQuestionNumber= String.valueOf(questionNumber + 1);
        String resultTotalQuestionNumber = String.valueOf(totalQuestionNumber + 1);
        String resultCount = String.valueOf(count + 1);

        // redis 저장
        saveNewQuestionDAOBean.exec(fieldKey, phase, resultCount, question,  resultQuestionNumber, resultTotalQuestionNumber);

        return saveSessionInfoDTOBean.exec(fieldId, question, resultQuestionNumber, resultTotalQuestionNumber);

    }
}
