package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;
import techtrek.domain.sessionInfo.service.bean.manager.CreateBasicManager;
import techtrek.domain.sessionInfo.service.bean.small.*;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.global.redis.service.bean.small.SaveNewQuestionDAOBean;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateStartInterviewBean {
    private final CreateBasicManager createBasicManager;

    private final GetUserDAOBean getUserDAOBean;
    private final SaveSessionInfoDAOBean saveSessionInfoDAOBean;
    private final SaveNewQuestionDAOBean saveNewQuestionDAOBean;

    private final CreateStartDTOBean saveSessionInfoDTOBean;

    // 면접 시작하기
    public SessionInfoResponse.Start exec(String enterpriseNameStr, String enterpriseTypeStr){
        // String -> Enum 변환 + 검증
        EnterpriseName enterpriseName = EnterpriseName.fromString(enterpriseNameStr);
        EnterpriseType enterpriseType = EnterpriseType.fromString(enterpriseTypeStr);

        // 사용자 조회
        User user = getUserDAOBean.exec("1");

        // 세션 생성
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = "interview:session:" + sessionId;
        String newkey = sessionKey +":new";
        String fieldKey = newkey+":" +fieldId;

        // 기본 질문 생성
        String question = createBasicManager.exec(enterpriseName);

        // 총 질문 번호, 질문 번호
        String questionNumber= "1";
        String totalQuestionNumber ="1";

        // redis 저장
        saveNewQuestionDAOBean.exec(fieldKey, "basic", "1", question,  questionNumber, totalQuestionNumber);

        // 세션정보 테이블에 값 저장
        saveSessionInfoDAOBean.exec(sessionId, enterpriseName, enterpriseType, user);

        return saveSessionInfoDTOBean.exec(sessionId, fieldId, question, questionNumber,totalQuestionNumber);
    }
}