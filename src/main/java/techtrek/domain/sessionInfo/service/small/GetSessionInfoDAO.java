package techtrek.domain.sessionInfo.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetSessionInfoDAO {
    private final SessionInfoRepository sessionInfoRepository;

    // 세션정보 조회
    public SessionInfo exec(String sessionId) {
        SessionInfo sessionInfo = sessionInfoRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        return sessionInfo;
    }

}
