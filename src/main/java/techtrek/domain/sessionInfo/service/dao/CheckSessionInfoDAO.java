package techtrek.domain.sessionInfo.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

@Component
@RequiredArgsConstructor
public class CheckSessionInfoDAO {

    private final SessionInfoRepository sessionInfoRepository;
    // 세션정보 존재 확인
    public void exec(String sessionId) {
        sessionInfoRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new CustomException(ErrorCode.SESSION_NOT_FOUND));
    }
}
