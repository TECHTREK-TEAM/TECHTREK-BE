package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.dto.SessionInfoResponse;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;
import techtrek.domain.sessionInfo.service.bean.helper.CreateBasicHelper;
import techtrek.domain.sessionInfo.service.bean.helper.SaveRedisNewHelper;
import techtrek.domain.sessionInfo.service.bean.small.*;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CreateStartInterviewBean {
    private final CreateBasicHelper createBasicHelper;
    private final SaveRedisNewHelper saveRedisNewHelper;

    private final GetUserDAOBean getUserDAOBean;
    private final SaveSessionInfoDAOBean saveSessionInfoDAOBean;

    // 면접 시작하기
    public SessionInfoResponse.Start exec(EnterpriseName enterpriseName, EnterpriseType enterpriseType){
        // 예외처리
        if (enterpriseName == null) throw new CustomException(ErrorCode.ENTERPRISE_NAME_NOT_FOUND);
        if (enterpriseType == null) throw new CustomException(ErrorCode.ENTERPRISE_TYPE_NOT_FOUND);

        // 사용자 정보 가져오기
        User user = getUserDAOBean.exec("1");

        // 세션 생성
        String sessionId = UUID.randomUUID().toString();
        String fieldId = UUID.randomUUID().toString();
        String sessionKey = "interview:session:" + sessionId;

        // 기본 질문 생성
        String basicQuestion = createBasicHelper.exec(enterpriseName);

        // redis 저장
        String questionNumber = saveRedisNewHelper.exec(sessionKey, fieldId, basicQuestion, "1", "basic");

        // 세션정보 테이블에 값 저장
        saveSessionInfoDAOBean.exec(sessionId, enterpriseName, enterpriseType, user);

        return new SessionInfoResponse.Start(sessionId,fieldId,basicQuestion,questionNumber);
    }
}
