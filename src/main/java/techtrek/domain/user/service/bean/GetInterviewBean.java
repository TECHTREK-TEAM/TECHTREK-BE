package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.service.small.GetInterviewHighScoreDAO;
import techtrek.domain.analysis.service.small.GetInterviewRecentScoreDAO;
import techtrek.domain.Interview.service.small.GetSessionInfoListDAO;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.CreateInterviewDTO;
import techtrek.domain.user.service.small.GetUserDAO;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetInterviewBean {

    private final GetUserDAO getUserDAO;
    private final GetSessionInfoListDAO getSessionInfoListDAO;
    private final GetInterviewHighScoreDAO getInterviewHighScoreDAO;
    private final GetInterviewRecentScoreDAO getInterviewRecentScoreDAO;
    private final CreateInterviewDTO createInterviewDTO;

    // 면접 정보(높은점수, 최근) 조회
    public UserResponse.Interview exec() {
        // TODO:사용자 조회
        User user = getUserDAO.exec("1");

        // 유저의 모든 세션 ID 조회
        List<String> sessionIds = getSessionInfoListDAO.exec(user.getId());

        // 해당 sessionId 중 analysis 가장 높은 점수 하나
        Analysis highestScoreAnalysis = getInterviewHighScoreDAO.exec(sessionIds);

        // 가장 최근 분석 하나
        Analysis recentAnalysis = getInterviewRecentScoreDAO.exec(sessionIds);

        // DTO 저장
        return createInterviewDTO.exec(user, highestScoreAnalysis, recentAnalysis);
    }
}
