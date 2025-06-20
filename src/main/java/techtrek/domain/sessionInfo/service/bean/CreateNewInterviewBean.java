package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionParserResponse;
import techtrek.domain.sessionInfo.service.dao.CheckSessionInfoDAO;
import techtrek.domain.sessionInfo.service.dto.CreateNewDTO;
import techtrek.domain.sessionInfo.service.dao.GetSessionInfoDAO;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.bean.common.CreateBasicUtil;
import techtrek.domain.sessionInfo.service.bean.common.CreateResumeUtil;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dao.GetUserDAO;
import techtrek.global.redis.service.dao.GetRedisDAO;
import techtrek.global.redis.service.dao.GetRedisTotalNumberDAO;
import techtrek.global.redis.service.dao.SaveNewQuestionDAO;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateNewInterviewBean {
    private final CreateBasicUtil createBasicUtil;
    private final CreateResumeUtil createResumeUtil;

    private final GetUserDAO getUserDAO;
    private final GetSessionInfoDAO getSessionInfoDAO;
    private final CheckSessionInfoDAO checkSessionInfoDAO;
    private final GetRedisDAO getRedisDAO;
    private final SaveNewQuestionDAO saveNewQuestionDAO;
    private final GetRedisTotalNumberDAO getRedisTotalNumberDAO;
    private final CreateNewDTO createNewDTO;

    // 새로운 질문 생성하기 ( 기본질문 or 이력서기반)
    public SessionInfoResponse.NewQuestion exec(String sessionId, String previousFieldId){
        // 세션 존재 확인
        checkSessionInfoDAO.exec(sessionId);

        // 사용자, 기업이름, 기업유형 조회
        User user = getUserDAO.exec("1");
        SessionInfo sessionInfo = getSessionInfoDAO.exec(sessionId);
        EnterpriseName enterpriseName = sessionInfo.getEnterpriseName();

        // 필드Id, 세션키 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = "interview:session:" + sessionId;
        String previousKey = sessionKey + ":new:" + previousFieldId;
        String fieldKey = sessionKey + ":new:" +fieldId;
        String question = "";

        // 이전 질문 조회
        SessionParserResponse.FieldData previousData = getRedisDAO.exec(previousKey);
        String phase = previousData.getPhase();
        int questionNumberData = Integer.parseInt(previousData.getQuestionNumber());
        int countData = Integer.parseInt(previousData.getCount());

        // 만약 count가 5이상이라면, 기본질문 <-> 이력서 기반 질문 변경
        if (countData >= 5) {
            phase = phase.equals("basic") ? "resume" : "basic";
            countData = 0;
        }

        // phase가 basic일 경우
        if (phase.equals("basic")) question = createBasicUtil.exec(enterpriseName);
        // phase가 resume일 경우
        else question = createResumeUtil.exec(user, sessionId);

        // 카운트, 질문 번호, 총 질문 번호
        String count = String.valueOf(countData + 1);
        String questionNumber= String.valueOf(questionNumberData + 1);
        int newCount = getRedisTotalNumberDAO.exec(sessionKey + ":new");
        int tailCount = getRedisTotalNumberDAO.exec(sessionKey + ":tail");
        int totalData = newCount + tailCount + 1;
        String totalQuestionNumber= String.valueOf(totalData);

        // redis 저장
        saveNewQuestionDAO.exec(fieldKey, phase, count, question,  questionNumber, totalQuestionNumber);

        return createNewDTO.exec(fieldId, question, questionNumber, totalQuestionNumber);

    }
}
