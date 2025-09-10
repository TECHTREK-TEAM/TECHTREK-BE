//package techtrek.domain.analysis.service.small;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import techtrek.domain.analysis.dto.AnalysisParserResponse;
//import techtrek.domain.analysis.entity.Analysis;
//import techtrek.domain.analysis.repository.AnalysisRepository;
//import techtrek.domain.Interview.entity.SessionInfo;
//import techtrek.domain.user.entity.User;
//
//import java.time.LocalDateTime;
//import java.util.UUID;
//
//@Component
//@RequiredArgsConstructor
//
//
//public class SaveAnalysisDAO {
//    private final AnalysisRepository analysisRepository;
//
//    // 분석 테이블 저장
//    public Analysis exec(SessionInfo sessionInfo, AnalysisParserResponse object, User user, int duration){
//        Analysis analysis = Analysis.builder()
//                .id(UUID.randomUUID().toString())
//                .status(object.getTotalScore() >= 70)
//                .resultScore(object.getTotalScore())
//                .followScore(object.getEvaluation().getFollowScore().getScore())
//                .result(object.getResult())
//                .keyword( object.getKeyKeywords().getKeyword())
//                .keywordNumber(object.getKeyKeywords().getQuestionNumber())
//                .analysisGroup(user.getUserGroup())
//                .duration(duration)
//                .createdAt(LocalDateTime.now().withNano(0))
//                .sessionInfo(sessionInfo)
//                .build();
//
//        analysisRepository.save(analysis);
//
//        return analysis;
//    }
//}