package techtrek.domain.analysis.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.service.small.DeleteAnalysisDAO;
import techtrek.global.redis.service.small.DeleteRedisDAO;
import techtrek.domain.session.entity.SessionInfo;
import techtrek.domain.session.service.small.DeleteSessionInfoDAO;
import techtrek.domain.session.service.small.GetSessionInfoDAO;

@Component
@RequiredArgsConstructor
public class DeleteAnalysisBean {
    private final GetSessionInfoDAO getSessionInfoDAO;
    private final DeleteAnalysisDAO deleteAnalysisDAO;
    private final DeleteRedisDAO deleteRedisDAO;
    private final DeleteSessionInfoDAO deleteSessionInfoDAO;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    public Boolean exec(String sessionInfoId){
        // 선택한 세션의 정보 조회
        SessionInfo sessionInfo = getSessionInfoDAO.execById(sessionInfoId);

        // 연관된 analysis 삭제
        deleteAnalysisDAO.exec(sessionInfo.getAnalysis().getId());

        // Redis 데이터 삭제
        String sessionId = sessionInfo.getSessionId();
        String redisKey = interviewPrefix + sessionId + "*";
        deleteRedisDAO.exec(redisKey);

        // sessionInfo 삭제
        deleteSessionInfoDAO.exec(sessionInfo);

        return true;
    }
}
