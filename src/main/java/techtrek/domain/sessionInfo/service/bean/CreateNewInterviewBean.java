package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.bean.manager.CreateBasicManager;
import techtrek.domain.sessionInfo.service.bean.manager.CreateResumeManager;
import techtrek.domain.sessionInfo.service.bean.small.*;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.global.bean.manager.CreateJsonReadManager;
import techtrek.global.bean.manager.CreateJsonWriteManager;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateNewInterviewBean {
    private final RedisTemplate<String, String> redisTemplate;

    private final CreateBasicManager createBasicManager;
    private final CreateResumeManager createResumeManager;
    private final CreateJsonReadManager createJsonReadManager;
    private final CreateJsonWriteManager createJsonWriteManager;

    private final GetUserDAOBean getUserDAOBean;
    private final CreateRedisDTOBean saveRedisDTOBean;
    private final CreateSessionInfoDTOBean saveSessionInfoDTOBean;
    private final GetSessionInfoDAOBean getSessionInfoDAOBean;
    private final CheckSessionInfoDAOBean checkSessionInfoDAOBean;

    // 새로운 질문 생성하기 ( 기본질문 or 이력서기반)
    public SessionInfoResponse.NewQuestion exec(String sessionId){
        // 세션 존재 확인
        checkSessionInfoDAOBean.exec(sessionId);

        // 사용자 조회
        User user = getUserDAOBean.exec("1");

        // 필드Id, 세션키 생성
        String sessionKey = "interview:session:" + sessionId;
        String fieldId = UUID.randomUUID().toString();
        String question = "";

        // 이전 질문 불러오기
        List<String> previousList = redisTemplate.opsForList().range(sessionKey + ":new", -1, -1);
        String previousJson = previousList.isEmpty() ? null : previousList.get(0);

        // 이전 질문 없을경우 예외처리
        if (previousJson == null) throw new CustomException(ErrorCode.PREVIOUS_QUESTION_NOT_FOUND);

        // 이전 질문의 phase, count값 불러오기
        RedisRequest.NewQuestion lastQuestion = createJsonReadManager.exec(previousJson, RedisRequest.NewQuestion.class);
        String phase = lastQuestion.getPhase();
        int count = Integer.parseInt(lastQuestion.getCount());

        // 만약 count가 5이상이라면, 기본질문 <-> 이력서 기반 질문 변경
        if (count >= 5) {
            phase = phase.equals("basic") ? "resume" : "basic";
            count = 0;
        }

        // phase가 basic일 경우
        if (phase.equals("basic")) {
            SessionInfo sessionInfo = getSessionInfoDAOBean.exec(sessionId);
            EnterpriseName enterpriseName = sessionInfo.getEnterpriseName();

            question = createBasicManager.exec(enterpriseName);
        }
        // phase가 resume일 경우
        else {
            question = createResumeManager.exec(user, sessionId);
        }

        // 질문 번호, 개수 계산 후, redis 저장용 DTO 생성
        String resultCount = String.valueOf(count + 1);
        RedisRequest.NewQuestion dto= saveRedisDTOBean.exec(sessionKey, fieldId, question, resultCount, phase);

        // JSON 직렬화 (객체 -> JSON)
        String json = createJsonWriteManager.exec(dto);

        // redis 저장
        redisTemplate.opsForList().rightPush(sessionKey + ":new", json);

        return saveSessionInfoDTOBean.exec(dto);

    }
}
