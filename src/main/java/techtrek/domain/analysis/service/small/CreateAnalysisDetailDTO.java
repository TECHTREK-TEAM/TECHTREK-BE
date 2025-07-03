package techtrek.domain.analysis.service.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.sessionInfo.dto.SessionParserResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;

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
        // 분석 결과 데이터
        AnalysisResponse.Detail.Analysis analysisResult = AnalysisResponse.Detail.Analysis.builder()
                .followScore(analysis.getFollowScore())
                .averageFollowPercent(followScoreDiffPercent)
                .duration(analysis.getDuration())
                .averageDurationPercent(durationDiffPercent)
                .resultScore(analysis.getResultScore())
                .topScore(topScorePercent)
                .status(analysis.isStatus())
                .build();

        // 인터뷰 데이터
        List<AnalysisResponse.Detail.Interview> interviewList = new ArrayList<>();
        for (SessionParserResponse.ListData data : listData) {
            interviewList.add(
                    AnalysisResponse.Detail.Interview.builder()
                            .question(data.getQuestion())
                            .answer(data.getAnswer())
                            .questionNumber(data.getQuestionNumber())
                            .build()
            );
        }

        // 피드백 데이터
        AnalysisResponse.Detail.Feedback feedback = AnalysisResponse.Detail.Feedback.builder()
                .keyword(analysis.getKeyword())
                .keywordNumber(analysis.getKeywordNumber())
                .result(analysis.getResult())
                .build();

        return new AnalysisResponse.Detail(sessionInfo.getId(), analysisResult, interviewList, feedback);
    }
}

