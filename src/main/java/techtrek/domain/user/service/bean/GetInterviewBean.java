package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.service.bean.small.GetHighScoreDAOBean;
import techtrek.domain.analysis.service.bean.small.GetRecentScoreDAOBean;
import techtrek.domain.sessionInfo.service.bean.small.GetSessionInfoListDAOBean;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.bean.small.GetInterviewDTOBean;
import techtrek.domain.user.service.bean.small.GetUserDAOBean;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetInterviewBean {

    private final GetUserDAOBean getUserDAOBean;
    private final GetSessionInfoListDAOBean getSessionInfoListDAOBean;
    private final GetHighScoreDAOBean getHighScoreDAOBean;
    private final GetRecentScoreDAOBean getRecentScoreDAOBean;
    private final GetInterviewDTOBean getInterviewDTOBean;

    public UserResponse.Interview exec() {
        // 사용자 조회
        User user = getUserDAOBean.exec("1");

        // 유저의 모든 세션 ID 조회
        List<String> sessionIds = getSessionInfoListDAOBean.exec(user.getId());

        // 해당 sessionId 중 analysis 가장 높은 점수 하나
        Analysis highestScoreAnalysis = getHighScoreDAOBean.exec(sessionIds);

        // 가장 최근 분석 하나
        Analysis recentAnalysis = getRecentScoreDAOBean.exec(sessionIds);

        // DTO 저장
        return getInterviewDTOBean.exec(user, highestScoreAnalysis, recentAnalysis);
    }
}
