package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.service.small.GetAnalysisDAO;
import techtrek.domain.sessionInfo.service.dao.GetSessionInfoDAO;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dao.GetUserDAO;

@Component
@RequiredArgsConstructor
public class GetPassBean {
    private final GetUserDAO getUserDAO;
    private final GetSessionInfoDAO getSessionInfoDAO;
    private final GetAnalysisDAO getAnalysisDAO;

    public UserResponse.Pass exec() {
        // 사용자 조회
        User user = getUserDAO.exec("1");

        // 전체 면접 수, 합격 면접 수 조회
        int InterviewTotal = getSessionInfoDAO.execCount(user.getId());
        int interviewPass = getAnalysisDAO.exec(user.getId());

        // 합격룰 계산
        double interviewPercent = InterviewTotal > 0
                ? Math.round(((double) interviewPass / InterviewTotal) * 1000) / 10.0 // 소수 첫째 자리까지
                : 0.0;

        return new UserResponse.Pass(InterviewTotal, interviewPass, interviewPercent);
    }
}