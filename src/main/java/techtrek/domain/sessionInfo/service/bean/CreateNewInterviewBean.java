package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisResponse;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.bean.helper.*;
import techtrek.domain.sessionInfo.service.bean.manager.CreateBasicManager;
import techtrek.domain.sessionInfo.service.bean.small.*;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateNewInterviewBean {
    private final GetRedisPreviousHelper getRedisPreviousHelper;
    private final CreateBasicManager createBasicHelper;
    private final CreateResumeHepler createResumeHepler;
    private final SaveRedisNewHelper saveRedisNewHelper;

    private final GetUserDAOBean getUserDAOBean;
    private final GetSessionInfoDAOBean getSessionInfoDAOBean;

    // 새로운 질문 생성하기 ( 기본질문 or 이력서기반)
    public SessionInfoResponse.NewQuestion exec(String sessionId){
        // 예외처리
        if (sessionId == null || sessionId.isEmpty()) throw new CustomException(ErrorCode.SESSION_NOT_FOUND);

        // 사용자 불러오기
        User user = getUserDAOBean.exec("1");

        // 필드Id, 세션키 생성
        String sessionKey = "interview:session:" + sessionId;
        String fieldId = UUID.randomUUID().toString();

        // 이전 질문의 count, phase 불러오기
        RedisResponse.PhaseCount PhaseCountDTO = getRedisPreviousHelper.exec(sessionKey);
        String question = "";
        String phase = PhaseCountDTO.getPhase();
        int count = PhaseCountDTO.getCount();

        // phase가 basic일 경우
        if (phase.equals("basic")) {
            // enterpriseName 불러오기
            SessionInfo sessionInfo = getSessionInfoDAOBean.exec(sessionId);
            EnterpriseName enterpriseName = sessionInfo.getEnterpriseName();

            question = createBasicHelper.exec(enterpriseName);
        }
        // phase가 resume일 경우
        else question = createResumeHepler.exec(user, sessionId);

        // redis 저장
        String resultCount = String.valueOf(count + 1);
        String questionNumber = saveRedisNewHelper.exec(sessionKey, fieldId, question, resultCount, phase);

        // 새로운 질문 반환
        return new SessionInfoResponse.NewQuestion(fieldId, question, questionNumber);

    }
}
