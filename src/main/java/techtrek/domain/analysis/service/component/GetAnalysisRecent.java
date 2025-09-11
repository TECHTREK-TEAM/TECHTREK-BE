package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisParserResponse;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.analysis.service.common.DBAnalysisCalc;
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

    // 최근 세션 불러오기
    public AnalysisResponse.Detail exec(String enterpriseName){
        // TODO: 사용자 조회
        User user = userRepository.findById("1")
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 기업 조회
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName)
                .orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));

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


        // sessionId를 이용해 redis에서 모든 필드의 totalCount, question, questionuber, anwer 반환
        // totalCount를 이요하여 오름차순


//        // 해당 기업의 세션정보 조회 (내림차순 후, 첫번째 세션정보 조회)
//        List<SessionInfo> sessionInfos = getSessionInfoListDAO.exec(user.getId(), enterpriseName);
//        if (sessionInfos == null || sessionInfos.isEmpty()) {
//            return createAnalysisDetailDTO.exec(null, null, 0.0, 0.0, 0.0, Collections.emptyList());
//        }
//        SessionInfo sessionInfo = sessionInfos.get(0);
//        if (sessionInfo == null) {
//            return createAnalysisDetailDTO.exec(null, null, 0.0, 0.0, 0.0, Collections.emptyList());
//        }
//
//        Analysis analysis = sessionInfo.getAnalysis();
//        if (analysis == null) {
//            return createAnalysisDetailDTO.exec(sessionInfo, null, 0.0, 0.0, 0.0, Collections.emptyList());
//        }
//
//        // 분석 데이터 수치
//        double followScore = analysis.getFollowScore();
//        int duration = analysis.getDuration();
//        double resultScore = analysis.getResultScore();
//
//        // 전체 평균 데이터
//        double avgFollowScore = getAverageFollowScoreDAO.exec(user.getId(),enterpriseName);
//        double avgDuration = getAvgDurationDAO.exec(user.getId(),enterpriseName);
//
//        // 퍼센트 차이 계산
//        double followScoreDiffPercent = ((followScore - avgFollowScore) / avgFollowScore) * 100;
//        followScoreDiffPercent = Math.round(followScoreDiffPercent * 10) / 10.0;
//        double durationDiffPercent = ((duration - avgDuration) / avgDuration) * 100;
//        durationDiffPercent = Math.round(durationDiffPercent * 10) / 10.0;
//
//        // 상위 점수 비율 계산
//        long totalCount = getEnterpriseAnalysisCountDAO.exec(enterpriseName);
//        long lowerCount = getEnterpriseAnalysisCountDAO.exec(enterpriseName, resultScore);
//        double topScorePercent = ((double)(lowerCount + 1) / totalCount) * 100;
//
//        // 모든 hash 데이터 조회
//        Set<String> allKeys = new HashSet<>();
//        allKeys.addAll(getRedisHashUtil.exec("interview:session:" + sessionInfo.getSessionId() + "*"));
//        List<SessionParserResponse.ListData> listData = getRedisByKeyDAO.exec(allKeys);
//
//        return createAnalysisDetailDTO.exec(sessionInfo, analysis, followScoreDiffPercent, durationDiffPercent, topScorePercent, listData);

        return AnalysisResponse.Detail.builder()
                .analysisId(DBResult.getAnalysisId())
                .analysis(AnalysisResponse.Detail.Analysis.builder()
                        .isPass(DBResult.getIsPass())
                        .score(DBResult.getScore())
                        .duration(DBResult.getDuration())
                        .averageDurationPercent(DBResult.getAverageDurationPercent())
                        .topScore(DBResult.getTopScore())
                        .build())
                .interview(List.of(
                        AnalysisResponse.Detail.Interview.builder()
                                .question("자기소개를 해주세요.")
                                .answer("저는 5년차 백엔드 개발자입니다.")
                                .questionNumber("1")
                                .build(),
                        AnalysisResponse.Detail.Interview.builder()
                                .question("Spring Boot를 사용해본 경험을 말해주세요.")
                                .answer("다양한 REST API와 JWT 인증을 구현했습니다.")
                                .questionNumber("2")
                                .build()
                ))
                .feedback(AnalysisResponse.Detail.Feedback.builder()
                        .keyword(DBResult.getKeyword())
                        .keywordNumber(DBResult.getKeywordNumber())
                        .feedback(DBResult.getFeedback())
                        .build())
                .build();

    }
}