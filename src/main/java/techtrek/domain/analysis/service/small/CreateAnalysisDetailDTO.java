package techtrek.domain.analysis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.session.dto.SessionParserResponse;
import techtrek.domain.session.entity.SessionInfo;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateAnalysisDetailDTO {

    // 세션 불러오기 DTO
    public AnalysisResponse.Detail exec(
            SessionInfo sessionInfo,
            Analysis analysis,
            double followScoreDiffPercent,
            double durationDiffPercent,
            double topScorePercent,
            List<SessionParserResponse.ListData> listData
    ) {
        // analysis가 null일 경우 기본값 세팅
        AnalysisResponse.Detail.Analysis analysisResult;
        if (analysis == null) {
            analysisResult = AnalysisResponse.Detail.Analysis.builder()
                    .followScore(0.0)
                    .averageFollowPercent(0.0)
                    .duration(0)
                    .averageDurationPercent(0.0)
                    .resultScore(0.0)
                    .topScore(0.0)
                    .status(false)
                    .build();
        } else {
            analysisResult = AnalysisResponse.Detail.Analysis.builder()
                    .followScore(analysis.getFollowScore())
                    .averageFollowPercent(followScoreDiffPercent)
                    .duration(analysis.getDuration())
                    .averageDurationPercent(durationDiffPercent)
                    .resultScore(analysis.getResultScore())
                    .topScore(topScorePercent)
                    .status(analysis.isStatus())
                    .build();
        }

        // 인터뷰 데이터
        List<AnalysisResponse.Detail.Interview> interviewList = new ArrayList<>();
        if (listData != null) {
            for (SessionParserResponse.ListData data : listData) {
                interviewList.add(
                        AnalysisResponse.Detail.Interview.builder()
                                .question(data.getQuestion())
                                .answer(data.getAnswer())
                                .questionNumber(data.getQuestionNumber())
                                .build()
                );
            }
        }

        // 피드백 데이터
        AnalysisResponse.Detail.Feedback feedback;
        if (analysis == null) {
            feedback = AnalysisResponse.Detail.Feedback.builder()
                    .keyword("")
                    .keywordNumber("0")
                    .result("")
                    .build();
        } else {
            feedback = AnalysisResponse.Detail.Feedback.builder()
                    .keyword(analysis.getKeyword())
                    .keywordNumber(analysis.getKeywordNumber())
                    .result(analysis.getResult())
                    .build();
        }

        return new AnalysisResponse.Detail(sessionInfo != null ? sessionInfo.getId() : null,
                analysisResult, interviewList, feedback);
    }

}

