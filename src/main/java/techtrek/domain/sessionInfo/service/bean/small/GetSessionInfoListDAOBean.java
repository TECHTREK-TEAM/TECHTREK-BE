package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetSessionInfoListDAOBean {
    private final SessionInfoRepository sessionInfoRepository;

    // 세션정보 List 조회
    public List<String> exec(String userId) {
        List<String> sessionIds = sessionInfoRepository.findSessionIdsByUserId(userId);
        return sessionIds;
    }

}