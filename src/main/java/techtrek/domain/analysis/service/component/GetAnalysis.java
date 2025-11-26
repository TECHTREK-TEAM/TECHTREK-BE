package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisParserResponse;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.analysis.service.common.DBAnalysisCalc;
import techtrek.domain.analysis.service.common.RedisAnalysisCalc;
import techtrek.domain.analysisQA.InterviewRecord;
import techtrek.domain.analysisQA.InterviewRecordRepository;
import techtrek.domain.analysisQA.InterviewSession;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAnalysis {
    private final AnalysisRepository analysisRepository;
    private final DBAnalysisCalc dbAnalysisCalc;
    private final RedisAnalysisCalc redisAnalysisCalc;
    private final InterviewRecordRepository interviewRecordRepository;

    // 선택한 세션 불러오기
    public AnalysisResponse.Detail exec(Long analysisId, CustomUserDetails userDetails){
        // Analysis 조회
        Analysis analysis = analysisRepository.findById(analysisId).orElseThrow(() -> new CustomException(ErrorCode.ANALYSIS_NOT_FOUND));

        // 권한체크
        if (!analysis.getUser().getId().equals(userDetails.getId())) throw new CustomException(ErrorCode.UNAUTHORIZED);

        // DB에서 분석 정보 계산
        AnalysisParserResponse.DBAnalysisResult DBResult = dbAnalysisCalc.exec(analysis.getEnterprise(), analysis );

        // redis에서 면접 내용 조회
//        List<AnalysisParserResponse.RedisAnalysisResult> RedisResult = redisAnalysisCalc.exec(DBResult.getSessionId());
//
//        // Interview 객체 빌드
//        List<AnalysisResponse.Detail.Interview> interviewList = RedisResult.stream()
//                .map(r -> AnalysisResponse.Detail.Interview.builder()
//                        .question(r.getQuestion())
//                        .answer(r.getAnswer())
//                        .questionNumber(r.getQuestionNumber())
//                        .build())
//                .toList();

        // Analysis에서 sessionId 가져오기
        InterviewSession interviewSession = analysis.getInterviewSession();

        // DB에서 면접 QA 조회
        List<InterviewRecord> qaList = interviewRecordRepository
                .findAllByInterviewSessionIdOrderByQuestionNumberAsc(interviewSession.getId());

        // Interview 객체 빌드
        List<AnalysisResponse.Detail.Interview> interviewList = qaList.stream()
                .map(q -> AnalysisResponse.Detail.Interview.builder()
                        .question(q.getQuestion())
                        .answer(q.getAnswer())
                        .questionNumber(q.getQuestionNumber())
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
