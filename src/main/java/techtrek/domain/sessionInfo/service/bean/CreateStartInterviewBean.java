package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.RedisRequest;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;
import techtrek.domain.sessionInfo.service.bean.manager.CreateBasicManager;
import techtrek.domain.sessionInfo.service.bean.small.*;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.global.bean.manager.CreateJsonWriteManager;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateStartInterviewBean {
    private final RedisTemplate<String, String> redisTemplate;

    private final CreateBasicManager createBasicManager;
    private final CreateJsonWriteManager createJsonWriteManager;

    private final SaveRedisDTOBean saveRedisDTOBean;
    private final GetUserDAOBean getUserDAOBean;
    private final SaveSessionInfoDAOBean saveSessionInfoDAOBean;

    // 면접 시작하기
    public SessionInfoResponse.Start exec(String enterpriseNameStr, String enterpriseTypeStr){

        // String -> Enum 변환 + 검증
        EnterpriseName enterpriseName = EnterpriseName.fromString(enterpriseNameStr);
        EnterpriseType enterpriseType = EnterpriseType.fromString(enterpriseTypeStr);

        // 사용자 정보 가져오기
        User user = getUserDAOBean.exec("1");

        // 세션 생성
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = "interview:session:" + sessionId;

        // 기본 질문 생성
        String basicQuestion = createBasicManager.exec(enterpriseName);

        // 질문 번호, 개수 계산 후, redis 저장용 DTO 생성
        RedisRequest.NewQuestion dto= saveRedisDTOBean.exec(sessionKey, fieldId, basicQuestion, "1", "basic");

        // JSON 직렬화 (객체 -> JSON)
        String json = createJsonWriteManager.exec(dto);

        // redis 저장
        redisTemplate.opsForList().rightPush(sessionKey + ":new", json);

        // 세션정보 테이블에 값 저장
        saveSessionInfoDAOBean.exec(sessionId, enterpriseName, enterpriseType, user);

        return new SessionInfoResponse.Start(sessionId,fieldId,basicQuestion,dto.getQuestionNumber());
    }
}
