package techtrek.domain.sessionInfo.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import techtrek.domain.redis.service.small.*;
import techtrek.domain.sessionInfo.dto.SessionParserResponse;
import techtrek.domain.sessionInfo.service.small.CreateNewDTO;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoDAO;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.common.CreateBasicUtil;
import techtrek.domain.sessionInfo.service.common.CreateResumeUtil;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.GetUserDAO;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateBasicInterview {
    // 상수 정의
    private static final String PHASE_BASIC = "basic";
    private static final String PHASE_RESUME = "resume";
    private static final int MAX_COUNT = 5;

    private final CreateBasicUtil createBasicUtil;
    private final CreateResumeUtil createResumeUtil;

    private final GetUserDAO getUserDAO;
    private final GetSessionInfoDAO getSessionInfoDAO;
    private final CheckRedisKeyDAO checkRedisKeyDAO;
    private final GetRedisDAO getRedisDAO;
    private final SaveNewQuestionDAO saveNewQuestionDAO;
    private final GetRedisTotalKeyCountDAO getRedisTotalKeyCountDAO;
    private final CreateNewDTO createNewDTO;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    // 새로운 질문 생성하기 ( 기본질문 or 이력서기반)
    public SessionInfoResponse.NewQuestion exec(String sessionId, String previousFieldId, CustomUserDetails userDetails){
        // 사용자, 기업이름 조회
        User user = getUserDAO.exec(userDetails.getId());
        SessionInfo sessionInfo = getSessionInfoDAO.exec(sessionId);
        EnterpriseName enterpriseName = sessionInfo.getEnterpriseName();

        // 권한체크
        if (!sessionInfo.getUser().getId().equals(userDetails.getId())) throw new CustomException(ErrorCode.UNAUTHORIZED);

        // 필드Id, 세션키 생성
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = interviewPrefix + sessionId;
        String previousKey = sessionKey + ":new:" + previousFieldId;
        String fieldKey = sessionKey + ":new:" +fieldId;

        // 이전필드Id 존재 확인
        if (!checkRedisKeyDAO.exec(previousKey)) { throw new CustomException(ErrorCode.PREVIOUS_FIELD_NOT_FOUND);}

        // 이전 질문 조회
        SessionParserResponse.FieldData previousData = getRedisDAO.exec(previousKey);

        // 질문 유형 조회, 변경
        String phase = updatePhase(previousData);

        // 질문 생성
        String question = PHASE_BASIC.equals(phase) ? createBasicUtil.exec(enterpriseName) : createResumeUtil.exec(user, sessionId);

        // 질문 관련 수치 계산
        String count = String.valueOf(Integer.parseInt(previousData.getCount()) + 1);
        String questionNumber = String.valueOf(Integer.parseInt(previousData.getQuestionNumber()) + 1);

        int newCount = getRedisTotalKeyCountDAO.exec(sessionKey + ":new");
        int tailCount = getRedisTotalKeyCountDAO.exec(sessionKey + ":tail");
        String totalQuestionNumber= String.valueOf(newCount + tailCount + 1);

        // redis에 정보 저장
        saveNewQuestionDAO.exec(fieldKey, phase, count, question, questionNumber, totalQuestionNumber);

        return createNewDTO.exec(fieldId, question, questionNumber, totalQuestionNumber);

    }

    // 질문 유형 조회, 변경
    private String updatePhase(SessionParserResponse.FieldData previousData) {
        String currentPhase = previousData.getPhase();
        int count = Integer.parseInt(previousData.getCount());

        // 만약 count가 MAX_COUNT 이상이라면, 기본질문 <-> 이력서 기반 질문 변경
        if (count >= MAX_COUNT) {
            return currentPhase.equals(PHASE_BASIC) ? PHASE_RESUME : PHASE_BASIC;
        }
        return currentPhase;
    }
}
