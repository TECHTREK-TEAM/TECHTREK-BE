package techtrek.domain.analysis.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dao.GetUserDAO;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAnalysisListBean {
    private final GetUserDAO getUserDAO;
    private final SessionInfoRepository sessionInfoRepository;

    public AnalysisResponse.SessionList exec(EnterpriseName enterpriseName){
        // 사용자 조회
        User user = getUserDAO.exec("1");

        // 세션정보 list 조회
        List<SessionInfo> sessionInfos = sessionInfoRepository
                .findTopByUserIdAndEnterpriseNameOrderByAnalysisCreatedAtDesc(user.getId(), enterpriseName);

        // dto 저장
        List<AnalysisResponse.SessionData> sessionDataList = new ArrayList<>();

        for (SessionInfo session : sessionInfos) {
            AnalysisResponse.SessionData data = AnalysisResponse.SessionData.builder()
                    .sessionInfoId(String.valueOf(session.getId()))
                    .enterpriseName(session.getEnterpriseName())
                    .createdAt(session.getAnalysis().getCreatedAt())
                    .build();

            sessionDataList.add(data);
        }

        return AnalysisResponse.SessionList.builder()
                .session(sessionDataList)
                .build();
    }
}
