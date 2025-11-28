package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.questionAnswer.entity.QuestionAnswer;
import techtrek.domain.questionAnswer.repository.QuestionAnswerRepository;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.domain.user.entity.User;
import techtrek.global.securty.service.CustomUserDetails;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAnalysisRecent {
    private final UserHelper userHelper;
    private final EnterpriseRepository enterpriseRepository;
    private final AnalysisRepository analysisRepository;
    private final QuestionAnswerRepository questionAnswerRepository;

    // 최근 세션 불러오기
    public AnalysisResponse.Detail exec(String enterpriseName, CustomUserDetails userDetails){
        // 사용자, 기업 조회
        User user = userHelper.validateUser(userDetails.getId());
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName).orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));

        // 최신 분석 데이터 조회
        Analysis latestAnalysis = analysisRepository.findTopByUserAndEnterpriseOrderByCreatedAtDesc(user, enterprise).orElse(null);

        // 분석 기록이 없는 경우 빈 DTO 반환
        if (latestAnalysis == null) {
            return AnalysisResponse.Detail.builder()
                    .analysisId(null)
                    .analysis(null)
                    .interview(List.of())
                    .feedback(null)
                    .build();
        }

        // QuestionAnswer 조회
        List<QuestionAnswer> qaList = getSortedQAList(latestAnalysis);

        // 평균 소요시간, 상위 % 계산
        double avgDurationPercent =
                calculateAverageDurationPercent(user, enterprise, latestAnalysis);
        double topScorePercent =
                calculateTopScorePercent(enterprise, latestAnalysis);

        // DTO 변환
        return buildResponse(latestAnalysis, qaList, avgDurationPercent, topScorePercent);

    }

    // QuestionAnswer 조회 및 정렬
    private List<QuestionAnswer> getSortedQAList(Analysis latestAnalysis) {
        List<QuestionAnswer> qaList = questionAnswerRepository.findByAnalysis(latestAnalysis);

        qaList.sort(Comparator
                .comparingInt(QuestionAnswer::getMainNumber)
                .thenComparingInt(QuestionAnswer::getSubNumber));

        return qaList;
    }

    // 유저의 평균 소요시간이 이번 세션 대비 몇 %인지 계산
    private double calculateAverageDurationPercent(User user, Enterprise enterprise, Analysis latestAnalysis) {

        List<Analysis> userAnalyses = analysisRepository.findByUserAndEnterprise(user, enterprise);

        double avgDuration = userAnalyses.stream()
                .mapToLong(Analysis::getDuration)
                .average()
                .orElse(latestAnalysis.getDuration());

        double percent = (avgDuration / latestAnalysis.getDuration()) * 100;

        return roundToOneDecimal(percent);
    }

    // 전체 사용자 중 해당 사용자 점수가 상위 몇 %인지 계산
    private double calculateTopScorePercent(Enterprise enterprise, Analysis latestAnalysis) {

        List<Analysis> all = analysisRepository.findByEnterprise(enterprise);

        if (all.isEmpty()) return 0;

        long lowerCount = all.stream()
                .filter(a -> a.getScore() < latestAnalysis.getScore())
                .count();

        double percent = ((double) lowerCount / all.size()) * 100;

        return roundToOneDecimal(percent);
    }

    // 소수 한 자리까지 반올림
    private double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }


    // DTO
    private AnalysisResponse.Detail buildResponse(
            Analysis latestAnalysis,
            List<QuestionAnswer> qaList,
            double avgDurationPercent,
            double topScorePercent) {

        var analysisDto = AnalysisResponse.Detail.Analysis.builder()
                .isPass(latestAnalysis.isPass())
                .score(latestAnalysis.getScore())
                .duration(latestAnalysis.getDuration())
                .averageDurationPercent(avgDurationPercent)
                .topScore(topScorePercent)
                .build();

        var feedbackDto = AnalysisResponse.Detail.Feedback.builder()
                .keyword(latestAnalysis.getKeyword())
                .keywordNumber(latestAnalysis.getKeywordNumber())
                .feedback(latestAnalysis.getFeedback())
                .build();

        var interviewList = qaList.stream()
                .map(qa -> AnalysisResponse.Detail.Interview.builder()
                        .question(qa.getQuestion())
                        .answer(qa.getAnswer())
                        .questionNumber(
                                qa.getSubNumber() == 0
                                        ? String.valueOf(qa.getMainNumber())
                                        : qa.getMainNumber() + "-" + qa.getSubNumber()
                        )
                        .build())
                .toList();

        return AnalysisResponse.Detail.builder()
                .analysisId(latestAnalysis.getId())
                .analysis(analysisDto)
                .interview(interviewList)
                .feedback(feedbackDto)
                .build();
    }
}