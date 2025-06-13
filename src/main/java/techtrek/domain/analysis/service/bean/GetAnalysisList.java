package techtrek.domain.analysis.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAnalysisList {
    private final GetUserDAOBean getUserDAOBean;
    private final SessionInfoRepository sessionInfoRepository;

    public AnalysisResponse.SessionList exec(EnterpriseName enterpriseName){
        // 사용자 조회
        User user = getUserDAOBean.exec("1");

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
