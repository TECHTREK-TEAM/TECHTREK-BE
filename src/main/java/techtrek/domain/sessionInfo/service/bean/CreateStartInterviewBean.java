package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.common.CreateBasicUtil;
import techtrek.domain.sessionInfo.service.small.CreateStartDTO;
import techtrek.domain.sessionInfo.service.small.SaveSessionInfoDAO;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.GetUserDAO;
import techtrek.domain.redis.service.small.SaveNewQuestionDAO;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateStartInterviewBean {
    // 상수 정의
    private static final String START_PHASE = "basic";
    private static final String START_COUNT = "1";
    private static final String START_QUESTION_NUMBER = "1";

    private final CreateBasicUtil createBasicUtil;

    private final GetUserDAO getUserDAO;
    private final SaveSessionInfoDAO saveSessionInfoDAO;
    private final SaveNewQuestionDAO saveNewQuestionDAO;
    private final CreateStartDTO createStartDTO;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    // 면접 시작하기
    public SessionInfoResponse.Start exec(EnterpriseName enterpriseName){
        // TODO: 사용자 조회
        User user = getUserDAO.exec("1");

        // 세션 생성
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String newkey = sessionKey +":new";
        String fieldKey = newkey+":"+fieldId;

        // 기본 질문 생성
        String question = createBasicUtil.exec(enterpriseName);

        // 총 질문 번호, 질문 번호
        String questionNumber= START_QUESTION_NUMBER;
        String totalQuestionNumber =START_QUESTION_NUMBER;

        // redis 저장
        saveNewQuestionDAO.exec(fieldKey, START_PHASE, START_COUNT, question,  questionNumber, totalQuestionNumber);

        // 세션정보 테이블에 값 저장
        String sessionInfoId =saveSessionInfoDAO.exec(sessionId, enterpriseName, user);

        return createStartDTO.exec(sessionId, fieldId, question, questionNumber,totalQuestionNumber,sessionInfoId);
    }
}