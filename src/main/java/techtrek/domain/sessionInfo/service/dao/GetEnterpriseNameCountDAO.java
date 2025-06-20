package techtrek.domain.sessionInfo.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetEnterpriseNameCountDAO {
    private final SessionInfoRepository sessionInfoRepository;

    // 세션정보 list 조회
    public List<SessionInfoRepository.EnterpriseCount> exec(User user) {
        List<SessionInfoRepository.EnterpriseCount> enterpriseCount =sessionInfoRepository.countByEnterpriseName(user);

        // 비어 있으면 빈 리스트 반환
        if (enterpriseCount.isEmpty()) {
            return Collections.emptyList();
        }

        return enterpriseCount;
    }
}
