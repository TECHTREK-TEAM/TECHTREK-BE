package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.service.small.CreateSessionListDTO;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoListDAO;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.GetUserDAO;
import techtrek.global.securty.service.CustomUserDetails;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAnalysisList {
    private final GetUserDAO getUserDAO;
    private final GetSessionInfoListDAO getSessionInfoListDAO;
    private final CreateSessionListDTO createSessionListDTO;

    // 세션 리스트 불러오기
    public AnalysisResponse.SessionList exec(EnterpriseName enterpriseName, CustomUserDetails userDetails){
        // 사용자 조회
        User user = getUserDAO.exec(userDetails.getId());

        // 해당 기업의 세션정보 list 조회 (내림차순)
        List<SessionInfo> sessionInfos = getSessionInfoListDAO.exec(user.getId(), enterpriseName);

        return createSessionListDTO.exec(sessionInfos);
    }
}
