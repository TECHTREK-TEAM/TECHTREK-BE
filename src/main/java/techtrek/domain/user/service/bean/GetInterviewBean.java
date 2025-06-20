package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.service.small.GetHighScoreDAO;
import techtrek.domain.analysis.service.small.GetRecentScoreDAO;
import techtrek.domain.sessionInfo.service.dao.GetSessionInfoListDAO;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dto.GetInterviewDTO;
import techtrek.domain.user.service.dao.GetUserDAO;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetInterviewBean {

    private final GetUserDAO getUserDAO;
    private final GetSessionInfoListDAO getSessionInfoListDAO;
    private final GetHighScoreDAO getHighScoreDAO;
    private final GetRecentScoreDAO getRecentScoreDAO;
    private final GetInterviewDTO getInterviewDTO;

    public UserResponse.Interview exec() {
        // 사용자 조회
        User user = getUserDAO.exec("1");

        // 유저의 모든 세션 ID 조회
        List<String> sessionIds = getSessionInfoListDAO.exec(user.getId());

        // 해당 sessionId 중 analysis 가장 높은 점수 하나
        Analysis highestScoreAnalysis = getHighScoreDAO.exec(sessionIds);

        // 가장 최근 분석 하나
        Analysis recentAnalysis = getRecentScoreDAO.exec(sessionIds);

        // DTO 저장
        return getInterviewDTO.exec(user, highestScoreAnalysis, recentAnalysis);
    }
}
