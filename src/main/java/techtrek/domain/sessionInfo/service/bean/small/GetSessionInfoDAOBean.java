package techtrek.domain.sessionInfo.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class GetSessionInfoDAOBean {
    private final SessionInfoRepository sessionInfoRepository;

    // 세션정보 조회
    public SessionInfo exec(String sessionId) {
        SessionInfo sessionInfo = sessionInfoRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
        return sessionInfo;
    }

    // userId를 이용하여 전체 면접 수 조회
    public int execCount(String userId) {
        int InterviewTotal = sessionInfoRepository.countAllSessions(userId);
        return InterviewTotal;
    }

}
