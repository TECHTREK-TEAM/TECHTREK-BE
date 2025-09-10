//package techtrek.domain.analysis.service.small;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import techtrek.domain.analysis.repository.AnalysisRepository;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class GetAverageTotalResultScoreDAO {
//    private final AnalysisRepository analysisRepository;
//
//    // 일치율 평균 (총) 조회
//    public Double exec(List<String> sessionIds){
//        return analysisRepository.findAverageScoreBySessionIds(sessionIds);
//    }
//}
