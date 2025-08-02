package techtrek.domain.sessionInfo.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import techtrek.domain.redis.service.small.DeleteRedisDAO;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.service.small.DeleteSessionInfoDAO;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoDAO;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;

@Component
@RequiredArgsConstructor
public class DeleteInterview {
    private final GetSessionInfoDAO getSessionInfoDAO;
    private final DeleteRedisDAO deleteRedisDAO;
    private final DeleteSessionInfoDAO deleteSessionInfoDAO;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    public Boolean exec(String sessionInfoId, CustomUserDetails userDetails){
        // 선택한 세션의 정보 조회
        SessionInfo sessionInfo = getSessionInfoDAO.execById(sessionInfoId);

        // 권한체크
        if (!sessionInfo.getUser().getId().equals(userDetails.getId())) throw new CustomException(ErrorCode.UNAUTHORIZED);

        // Redis 데이터 삭제
        String sessionId = sessionInfo.getSessionId();
        String redisKey = interviewPrefix + sessionId + "*";
        deleteRedisDAO.exec(redisKey);

        // sessionInfo 삭제
        deleteSessionInfoDAO.exec(sessionInfo);

        return true;
    }
}

