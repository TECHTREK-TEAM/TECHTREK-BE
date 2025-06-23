package techtrek.domain.sessionInfo.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetSessionInfoListDAO {
    private final SessionInfoRepository sessionInfoRepository;

    // 세션정보 List 조회: sessionId 반환
    public List<String> exec(String userId) {
        List<String> sessionIds = sessionInfoRepository.findSessionIdsByUserId(userId);
        return sessionIds;
    }

    // 세션정보 list 조회: sessionInfo 반환
    public List<SessionInfo> exec(User user) {
        List<SessionInfo> sessionInfos = sessionInfoRepository.findAllByUser(user);
        return sessionInfos;
    }

    // 세션정보 List 조회: sessionInfo 반환 (enterprise + 내림차순)
    public List<SessionInfo> exec(String userId, EnterpriseName enterpriseName) {
        List<SessionInfo> sessionInfos = sessionInfoRepository
                .findTopByUserIdAndEnterpriseNameOrderByAnalysisCreatedAtDesc(userId, enterpriseName);

        return sessionInfos;
    }

}