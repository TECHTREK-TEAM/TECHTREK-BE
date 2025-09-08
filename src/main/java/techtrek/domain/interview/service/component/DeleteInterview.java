package techtrek.domain.interview.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import techtrek.global.redis.service.small.DeleteRedisDAO;
import techtrek.domain.interview.entity.SessionInfo;
import techtrek.domain.interview.service.small.DeleteSessionInfoDAO;
import techtrek.domain.interview.service.small.GetSessionInfoDAO;

@Component
@RequiredArgsConstructor
public class DeleteInterview {
    private final GetSessionInfoDAO getSessionInfoDAO;
    private final DeleteRedisDAO deleteRedisDAO;
    private final DeleteSessionInfoDAO deleteSessionInfoDAO;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    public Boolean exec(String sessionInfoId){
        // 선택한 세션의 정보 조회
        SessionInfo sessionInfo = getSessionInfoDAO.execById(sessionInfoId);

        // Redis 데이터 삭제
        String sessionId = sessionInfo.getSessionId();
        String redisKey = interviewPrefix + sessionId + "*";
        deleteRedisDAO.exec(redisKey);

        // sessionInfo 삭제
        deleteSessionInfoDAO.exec(sessionInfo);

        return true;
    }
}

