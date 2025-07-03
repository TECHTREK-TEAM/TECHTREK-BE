package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.service.small.GetPassCountDAO;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoCountDAO;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.CreatePassDTO;
import techtrek.domain.user.service.small.GetUserDAO;

@Component
@RequiredArgsConstructor
public class GetPassBean {
    private final GetUserDAO getUserDAO;
    private final GetSessionInfoCountDAO getSessionInfoCountDAO;
    private final GetPassCountDAO getPassCountDAO;
    private final CreatePassDTO createPassDTO;

    // 전체 합격률 조회
    public UserResponse.Pass exec() {
        // TODO:사용자 조회
        User user = getUserDAO.exec("1");

        // 전체 면접 수, 합격 면접 수 조회
        int interviewTotal = getSessionInfoCountDAO.exec(user.getId());
        int interviewPass = getPassCountDAO.exec(user.getId());

        // 합격룰 계산
        double interviewPercent = 0.0;
        if (interviewTotal > 0) {
            interviewPercent = Math.round(((double) interviewPass / interviewTotal) * 1000) / 10.0;
        }

        return createPassDTO.exec(interviewTotal, interviewPass, interviewPercent);
    }
}