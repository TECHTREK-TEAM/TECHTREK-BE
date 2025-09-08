package techtrek.domain.interview.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.interview.entity.SessionInfo;
import techtrek.domain.basicQuestion.entity.status.EnterpriseName;
import techtrek.domain.interview.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SaveSessionInfoDAO {

    private final SessionInfoRepository sessionInfoRepository;

    // 세션정보 테이블 저장
    public String exec(String sessionId, EnterpriseName enterpriseName, User user){
        SessionInfo sessionInfo = SessionInfo.builder()
                .id(UUID.randomUUID().toString())
                .sessionId(sessionId)
                .enterpriseName(enterpriseName)
                .user(user)
                .build();

        sessionInfoRepository.save(sessionInfo);

        return sessionInfo.getId();
    }
}
