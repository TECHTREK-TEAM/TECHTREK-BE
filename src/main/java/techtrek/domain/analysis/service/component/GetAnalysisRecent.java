package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisParserResponse;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.analysis.service.common.DBAnalysisCalc;
import techtrek.domain.analysis.service.common.RedisAnalysisCalc;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.domain.user.entity.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAnalysisRecent {
    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final AnalysisRepository analysisRepository;
    private final DBAnalysisCalc dbAnalysisCalc;
    private final RedisAnalysisCalc redisAnalysisCalc;

    // 최근 세션 불러오기
    public AnalysisResponse.Detail exec(String enterpriseName){
        // TODO: 사용자 조회
        User user = userRepository.findById("1").orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 기업 조회
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName).orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));

        // 최신 분석 데이터 조회
        Analysis latestAnalysis = analysisRepository.findTopByUserAndEnterpriseOrderByCreatedAtDesc(
                user, enterprise
        ).orElse(null);

        if (latestAnalysis == null) {
            return AnalysisResponse.Detail.builder()
                    .analysisId(null)
                    .analysis(null)
                    .interview(List.of())
                    .feedback(null)
                    .build();
        }

        // DB에서 분석 정보 계산
        AnalysisParserResponse.DBAnalysisResult DBResult = dbAnalysisCalc.exec(user, enterprise, latestAnalysis );

        // redis에서 면접 내용 조회
        List<AnalysisParserResponse.RedisAnalysisResult> RedisResult = redisAnalysisCalc.exec(DBResult.getSessionId());

        // Interview 객체 빌드
        List<AnalysisResponse.Detail.Interview> interviewList = RedisResult.stream()
                .map(r -> AnalysisResponse.Detail.Interview.builder()
                        .question(r.getQuestion())
                        .answer(r.getAnswer())
                        .questionNumber(r.getQuestionNumber())
                        .build())
                .toList();

        // Detail 객체 빌드
        return AnalysisResponse.Detail.builder()
                .analysisId(DBResult.getAnalysisId())
                .analysis(AnalysisResponse.Detail.Analysis.builder()
                        .isPass(DBResult.getIsPass())
                        .score(DBResult.getScore())
                        .duration(DBResult.getDuration())
                        .averageDurationPercent(DBResult.getAverageDurationPercent())
                        .topScore(DBResult.getTopScore())
                        .build())
                .interview(interviewList)
                .feedback(AnalysisResponse.Detail.Feedback.builder()
                        .keyword(DBResult.getKeyword())
                        .keywordNumber(DBResult.getKeywordNumber())
                        .feedback(DBResult.getFeedback())
                        .build())
                .build();

    }
}