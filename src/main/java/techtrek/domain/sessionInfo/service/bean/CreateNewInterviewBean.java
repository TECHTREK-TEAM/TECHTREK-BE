package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.global.redis.dto.RedisResponse;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.bean.manager.CreateBasicManager;
import techtrek.domain.sessionInfo.service.bean.manager.CreateResumeManager;
import techtrek.domain.sessionInfo.service.bean.small.*;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.global.redis.service.bean.small.GetRedisDAOBean;
import techtrek.global.redis.service.bean.small.GetRedisTotalNumberDAOBean;
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
    private final GetRedisDAOBean getRedisDAOBean;
    private final SaveNewQuestionDAOBean saveNewQuestionDAOBean;
    private final GetRedisTotalNumberDAOBean getRedisTotalNumberDAOBean;

    private final CreateNewDTOBean createNewDTOBean;

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
        String fieldKey = sessionKey + ":new:" +fieldId;
        String question = "";

        // 이전 질문 조회
        RedisResponse.FieldData previousData = getRedisDAOBean.exec(previousKey);
        String phase = previousData.getPhase();
        int questionNumberData = Integer.parseInt(previousData.getQuestionNumber());
        int countData = Integer.parseInt(previousData.getCount());

        // 만약 count가 5이상이라면, 기본질문 <-> 이력서 기반 질문 변경
        if (countData >= 5) {
            phase = phase.equals("basic") ? "resume" : "basic";
            countData = 0;
        }

        // phase가 basic일 경우
        if (phase.equals("basic")) question = createBasicManager.exec(enterpriseName);
        // phase가 resume일 경우
        else question = createResumeManager.exec(user, sessionId);

        // 카운트, 질문 번호, 총 질문 번호
        String count = String.valueOf(countData + 1);
        String questionNumber= String.valueOf(questionNumberData + 1);
        int newCount = getRedisTotalNumberDAOBean.exec(sessionKey + ":new");
        int tailCount = getRedisTotalNumberDAOBean.exec(sessionKey + ":tail");
        int totalData = newCount + tailCount + 1;
        String totalQuestionNumber= String.valueOf(totalData);

        // redis 저장
        saveNewQuestionDAOBean.exec(fieldKey, phase, count, question,  questionNumber, totalQuestionNumber);

        return createNewDTOBean.exec(fieldId, question, questionNumber, totalQuestionNumber);

    }
}
