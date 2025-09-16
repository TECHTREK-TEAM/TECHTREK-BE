package techtrek.domain.sessionInfo.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import techtrek.domain.redis.service.small.CheckRedisKeyDAO;
import techtrek.domain.redis.service.small.SaveAnswerDAO;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoDAO;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;

@Component
@RequiredArgsConstructor
public class CreateAnswer {
    private final SaveAnswerDAO saveAnswerDAO;
    private final CheckRedisKeyDAO checkRedisKeyDAO;
    private final GetSessionInfoDAO getSessionInfoDAO;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    // 답변하기
    public Boolean exec(String sessionId, String fieldId, String type, String answer, CustomUserDetails userDetails){
        // 권한 체크
        SessionInfo sessionInfo = getSessionInfoDAO.exec(sessionId);
        if (!sessionInfo.getUser().getId().equals(userDetails.getId())) throw new CustomException(ErrorCode.UNAUTHORIZED);

        // 키 생성
        String fieldKey = interviewPrefix + sessionId + ":" + type + ":"+ fieldId;

        // 해당 키 존재 확인
        if (!checkRedisKeyDAO.exec(fieldKey)) { throw new CustomException(ErrorCode.FIELD_NOT_FOUND);}

        // 답변 저장
        saveAnswerDAO.exec(fieldKey,answer);

        return true;
    };
}
