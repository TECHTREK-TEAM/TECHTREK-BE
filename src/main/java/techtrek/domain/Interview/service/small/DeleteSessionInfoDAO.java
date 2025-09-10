package techtrek.domain.Interview.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.entity.SessionInfo;
import techtrek.domain.Interview.repository.SessionInfoRepository;

@Component
@RequiredArgsConstructor
public class DeleteSessionInfoDAO {
    private final SessionInfoRepository sessionInfoRepository;

    // 세션 정보 삭제
    public void exec(SessionInfo sessionInfo){
        sessionInfoRepository.delete(sessionInfo);
    }
}
