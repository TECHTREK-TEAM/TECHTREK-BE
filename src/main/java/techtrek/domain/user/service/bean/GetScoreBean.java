package techtrek.domain.user.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.service.small.GetAverageResultScoreDAO;
import techtrek.domain.analysis.service.small.GetTotalAverageResultScoreDAO;
import techtrek.domain.sessionInfo.service.dao.GetSessionInfoListDAO;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dao.GetUserDAO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetScoreBean {
    private final GetUserDAO getUserDAO;
    private final GetSessionInfoListDAO getSessionInfoListDAO;
    private final GetTotalAverageResultScoreDAO getTotalAverageResultScoreDAO;
    private final GetAverageResultScoreDAO getAverageResultScoreDAO;

    public UserResponse.Score exec(){
        // 사용자 조회
        User user = getUserDAO.exec("1");

        // 유저의 모든 세션 ID 조회
        List<String> sessionIds = getSessionInfoListDAO.exec(user.getId());

        // 전체 평균 점수
        Double average = getTotalAverageResultScoreDAO.exec(sessionIds);
        if (average == null) average = 0.0;

        // 범위
        LocalDateTime startOfCurrentMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfPreviousMonth = startOfCurrentMonth.minusMonths(1);
        LocalDateTime endOfPreviousMonth = startOfCurrentMonth.minusSeconds(1);

        // 저번 달, 이번 달 평균
        Double lastMonthAvg = getAverageResultScoreDAO.exec( sessionIds, startOfPreviousMonth, endOfPreviousMonth);
        Double thisMonthAvg =  getAverageResultScoreDAO.exec(sessionIds, startOfCurrentMonth, LocalDateTime.now());

        // 증가율 계산 (this - last) / last * 100
        double enhancedPercent = 0.0;
        if (lastMonthAvg != null && lastMonthAvg != 0.0) {
            enhancedPercent = ((thisMonthAvg - lastMonthAvg) / lastMonthAvg) * 100;
        }

        // 소수점 첫째자리
        average = Math.round(average * 10) / 10.0;
        enhancedPercent = Math.round(enhancedPercent * 10) / 10.0;

        return new UserResponse.Score(average, enhancedPercent);
    }
}
