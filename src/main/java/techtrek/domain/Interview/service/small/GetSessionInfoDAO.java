package techtrek.domain.Interview.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.Interview.entity.SessionInfo;
import techtrek.domain.Interview.repository.SessionInfoRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;


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

    // 세션정보 조회
    public SessionInfo execById(String sessionInfoId) {
        SessionInfo sessionInfo = sessionInfoRepository.findById(sessionInfoId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        return sessionInfo;
    }

}
