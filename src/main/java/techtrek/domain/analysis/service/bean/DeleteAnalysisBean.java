package techtrek.domain.analysis.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoDAO;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class DeleteAnalysisBean {
    private final GetSessionInfoDAO getSessionInfoDAO;
    private final AnalysisRepository analysisRepository;
    private final SessionInfoRepository sessionInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${custom.redis.prefix.interview}")
    private String interviewPrefix;

    public Boolean exec(String sessionInfoId){

        SessionInfo sessionInfo = getSessionInfoDAO.execById(sessionInfoId);

        // 2. 연관된 analysis 삭제
        analysisRepository.deleteById(sessionInfo.getAnalysis().getId());

        // 3. Redis 데이터 삭제
        String sessionId = sessionInfo.getSessionId();
        String redisKeyPattern = interviewPrefix + sessionId + "*";
        Set<String> keys = redisTemplate.keys(redisKeyPattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }

        // 4. sessionInfo 삭제
        sessionInfoRepository.delete(sessionInfo);

        return true;


    }

}
