package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.service.small.GetAverageResultScoreDAO;
import techtrek.domain.analysis.service.small.GetAverageTotalResultScoreDAO;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoListDAO;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.CreateScoreDTO;
import techtrek.domain.user.service.small.GetUserDAO;
import techtrek.global.securty.service.CustomUserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetScore {
    private final GetUserDAO getUserDAO;
    private final GetSessionInfoListDAO getSessionInfoListDAO;
    private final GetAverageTotalResultScoreDAO getAverageTotalResultScoreDAO;
    private final GetAverageResultScoreDAO getAverageResultScoreDAO;
    private final CreateScoreDTO createScoreDTO;

    // 일치율 조회
    public UserResponse.Score exec(CustomUserDetails userDetails){
        // 사용자 조회
        User user = getUserDAO.exec(userDetails.getId());

        // 유저의 모든 세션 ID 조회
        List<String> sessionIds = getSessionInfoListDAO.exec(user.getId());

        // 전체 평균 점수
        double average = checkScore(getAverageTotalResultScoreDAO.exec(sessionIds));

        // 범위
        LocalDateTime startOfCurrentMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
        LocalDateTime startOfPreviousMonth = startOfCurrentMonth.minusMonths(1);
        LocalDateTime endOfPreviousMonth = startOfCurrentMonth.minusSeconds(1);

        // 저번 달, 이번 달 평균
        double lastMonthAvg = checkScore(getAverageResultScoreDAO.exec(sessionIds, startOfPreviousMonth, endOfPreviousMonth));
        double thisMonthAvg = checkScore(getAverageResultScoreDAO.exec(sessionIds, startOfCurrentMonth, LocalDateTime.now()));


        // 증가율 계산 (this - last) / last * 100
        double enhancedPercent = lastMonthAvg != 0.0 ? ((thisMonthAvg - lastMonthAvg) / lastMonthAvg) * 100 : 0.0;

        // 소수점 첫째자리
        average = Math.round(average * 10) / 10.0;
        enhancedPercent = Math.round(enhancedPercent * 10) / 10.0;

        return createScoreDTO.exec(average, enhancedPercent);
    }

    // score null 체크
    private double checkScore(Double score) {
        return score != null ? score : 0.0;
    }
}
