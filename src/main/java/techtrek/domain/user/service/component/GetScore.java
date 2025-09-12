package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
//import techtrek.domain.analysis.service.small.GetAverageResultScoreDAO;
//import techtrek.domain.analysis.service.small.GetAverageTotalResultScoreDAO;
//import techtrek.domain.Interview.service.small.GetSessionInfoListDAO;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
//import techtrek.domain.user.service.small.CreateScoreDTO;
import techtrek.domain.user.service.small.GetUserDAO;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetScore {
    private final UserRepository userRepository;
    private final AnalysisRepository analysisRepository;
    private final GetUserDAO getUserDAO;
//    private final GetSessionInfoListDAO getSessionInfoListDAO;
//    private final GetAverageTotalResultScoreDAO getAverageTotalResultScoreDAO;
//    private final GetAverageResultScoreDAO getAverageResultScoreDAO;
//    private final CreateScoreDTO createScoreDTO;

    // 일치율 조회
    public UserResponse.Score exec(){
        // TODO:사용자 조회
        User user = userRepository.findById("1").orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

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


        // 유저의 분석 데이터 score 평균
        // 이번달이 저번달 대비 score 얼만큼 올랐는지 %


        // 유저의 모든 세션 ID 조회
//        List<String> sessionIds = getSessionInfoListDAO.exec(user.getId());
//
//        // 전체 평균 점수
//        double average = checkScore(getAverageTotalResultScoreDAO.exec(sessionIds));
//
//        // 범위
//        LocalDateTime startOfCurrentMonth = LocalDate.now().withDayOfMonth(1).atStartOfDay();
//        LocalDateTime startOfPreviousMonth = startOfCurrentMonth.minusMonths(1);
//        LocalDateTime endOfPreviousMonth = startOfCurrentMonth.minusSeconds(1);
//
//        // 저번 달, 이번 달 평균
//        double lastMonthAvg = checkScore(getAverageResultScoreDAO.exec(sessionIds, startOfPreviousMonth, endOfPreviousMonth));
//        double thisMonthAvg = checkScore(getAverageResultScoreDAO.exec(sessionIds, startOfCurrentMonth, LocalDateTime.now()));
//
//
//        // 증가율 계산 (this - last) / last * 100
//        double enhancedPercent = lastMonthAvg != 0.0 ? ((thisMonthAvg - lastMonthAvg) / lastMonthAvg) * 100 : 0.0;
//
//        // 소수점 첫째자리
//        average = Math.round(average * 10) / 10.0;
//        enhancedPercent = Math.round(enhancedPercent * 10) / 10.0;
//
//        return createScoreDTO.exec(average, enhancedPercent);
        return UserResponse.Score.builder()
                .totalAvgScore(avgScore)
                .enhancedPercent(increasePercent)
                .build();
    }

//    // score null 체크
//    private double checkScore(Double score) {
//        return score != null ? score : 0.0;
//    }
}
