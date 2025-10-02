package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

import java.time.YearMonth;

@Component
@RequiredArgsConstructor
public class GetScore {
    private final UserValidator userValidator;
    private final AnalysisRepository analysisRepository;

    // 일치율 조회
    public UserResponse.Score exec(CustomUserDetails userDetails){
        // 사용자 조회
        User user = userValidator.validateAndGetUser(userDetails.getId());

        // 전체 평균 score
        Double avgScore = analysisRepository.findAvgScoreByUser(user.getId());
        avgScore = avgScore != null ? Math.round(avgScore * 10) / 10.0 : 0.0;

        // 이번 달, 저번 달 평균 score 조회
        YearMonth now = YearMonth.now();
        YearMonth lastMonth = now.minusMonths(1);

        Double thisMonthAvg = analysisRepository.findAvgScoreByUserAndMonth(user.getId(), now.getYear(), now.getMonthValue());
        Double lastMonthAvg = analysisRepository.findAvgScoreByUserAndMonth(user.getId(), lastMonth.getYear(), lastMonth.getMonthValue());

        thisMonthAvg = thisMonthAvg != null ? thisMonthAvg : 0.0;
        lastMonthAvg = lastMonthAvg != null ? lastMonthAvg : 0.0;

        // 상승률 계산
        double increasePercent = (lastMonthAvg == 0) ? 0.0
                : Math.round(((thisMonthAvg - lastMonthAvg) / lastMonthAvg) * 1000) / 10.0;

        return UserResponse.Score.builder()
                .totalAvgScore(avgScore)
                .enhancedPercent(increasePercent)
                .build();
    }

}
