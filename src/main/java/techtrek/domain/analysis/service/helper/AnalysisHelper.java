package techtrek.domain.analysis.service.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.questionAnswer.entity.QuestionAnswer;
import techtrek.domain.questionAnswer.repository.QuestionAnswerRepository;
import techtrek.domain.user.entity.User;

import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnalysisHelper {
    private final QuestionAnswerRepository questionAnswerRepository;
    private final AnalysisRepository analysisRepository;


    // 유저의 평균 소요시간이 이번 세션 대비 몇 %인지 계산
    public double calculateAverageDurationPercent(User user, Enterprise enterprise, Analysis latestAnalysis) {

        List<Analysis> userAnalyses = analysisRepository.findByUserAndEnterprise(user, enterprise);

        double avgDuration = userAnalyses.stream()
                .mapToLong(Analysis::getDuration)
                .average()
                .orElse(latestAnalysis.getDuration());

        double percent = (avgDuration / latestAnalysis.getDuration()) * 100;

        return roundToOneDecimal(percent);
    }

    // 전체 사용자 중 해당 사용자 점수가 상위 몇 %인지 계산
    public double calculateTopScorePercent(Enterprise enterprise, Analysis latestAnalysis) {

        List<Analysis> all = analysisRepository.findByEnterprise(enterprise);

        if (all.isEmpty()) return 0;

        long lowerCount = all.stream()
                .filter(a -> a.getScore() < latestAnalysis.getScore())
                .count();

        double percent = ((double) lowerCount / all.size()) * 100;

        return roundToOneDecimal(percent);
    }

    // 소수 한 자리까지 반올림
    public double roundToOneDecimal(double value) {
        return Math.round(value * 10.0) / 10.0;
    }



    // QuestionAnswer 조회 및 정렬
    public List<QuestionAnswer> getSortedQAList(Analysis latestAnalysis) {
        List<QuestionAnswer> qaList = questionAnswerRepository.findByAnalysis(latestAnalysis);

        qaList.sort(Comparator
                .comparingInt(QuestionAnswer::getMainNumber)
                .thenComparingInt(QuestionAnswer::getSubNumber));

        return qaList;
    }

    // DTO
    public AnalysisResponse.Detail buildResponse(
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
