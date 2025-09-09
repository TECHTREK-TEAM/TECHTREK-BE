package techtrek.domain.session.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.session.repository.SessionInfoRepository;

@Component
@RequiredArgsConstructor
public class GetSessionInfoCountDAO {
    private final SessionInfoRepository sessionInfoRepository;

    // 전체 면접 수 조회
    public int exec(String userId) {
        int InterviewTotal = sessionInfoRepository.countAllSessions(userId);
        return InterviewTotal;
    }
}
