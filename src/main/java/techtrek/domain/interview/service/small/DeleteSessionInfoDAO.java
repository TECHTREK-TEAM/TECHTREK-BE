package techtrek.domain.interview.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.interview.entity.SessionInfo;
import techtrek.domain.interview.repository.SessionInfoRepository;

@Component
@RequiredArgsConstructor
public class DeleteSessionInfoDAO {
    private final SessionInfoRepository sessionInfoRepository;

    // 세션 정보 삭제
    public void exec(SessionInfo sessionInfo){
        sessionInfoRepository.delete(sessionInfo);
    }
}
