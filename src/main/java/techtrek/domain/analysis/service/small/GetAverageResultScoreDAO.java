//package techtrek.domain.analysis.service.small;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import techtrek.domain.analysis.repository.AnalysisRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class GetAverageResultScoreDAO {
//    private final AnalysisRepository analysisRepository;
//
//    // 일치율 평균 (해당 달) 조회
//    public Double exec(List<String> sessionIds, LocalDateTime startMonth, LocalDateTime endMonth){
//        return analysisRepository.findAverageScoreBySessionIdsAndDateRange(
//                sessionIds, startMonth, endMonth);
//    }
//}
