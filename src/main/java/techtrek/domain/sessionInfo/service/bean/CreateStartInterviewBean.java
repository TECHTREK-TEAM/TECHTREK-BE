package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.bean.common.CreateBasicUtil;
import techtrek.domain.sessionInfo.service.dto.CreateStartDTO;
import techtrek.domain.sessionInfo.service.dao.SaveSessionInfoDAO;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dao.GetUserDAO;
import techtrek.domain.redis.service.dao.SaveNewQuestionDAO;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateStartInterviewBean {
    private final CreateBasicUtil createBasicUtil;

    private final GetUserDAO getUserDAO;
    private final SaveSessionInfoDAO saveSessionInfoDAO;
    private final SaveNewQuestionDAO saveNewQuestionDAO;
    private final CreateStartDTO createStartDTO;

    // 면접 시작하기
    public SessionInfoResponse.Start exec(String enterpriseNameStr){
        // String -> Enum 변환 + 검증
        EnterpriseName enterpriseName = EnterpriseName.fromString(enterpriseNameStr);

        // 사용자 조회
        User user = getUserDAO.exec("1");

        // 세션 생성
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = "interview:session:" + sessionId;
        String newkey = sessionKey +":new";
        String fieldKey = newkey+":" +fieldId;

        // 기본 질문 생성
        String question = createBasicUtil.exec(enterpriseName);

        // 총 질문 번호, 질문 번호
        String questionNumber= "1";
        String totalQuestionNumber ="1";

        // redis 저장
        saveNewQuestionDAO.exec(fieldKey, "basic", "1", question,  questionNumber, totalQuestionNumber);

        // 세션정보 테이블에 값 저장
        saveSessionInfoDAO.exec(sessionId, enterpriseName, user);

        return createStartDTO.exec(sessionId, fieldId, question, questionNumber,totalQuestionNumber);
    }
}