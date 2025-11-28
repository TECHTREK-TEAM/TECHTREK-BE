package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.analysis.service.helper.AnalysisHelper;
import techtrek.domain.questionAnswer.entity.QuestionAnswer;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAnalysis {
    private final AnalysisHelper analysisHelper;
    private final UserHelper userHelper;
    private final AnalysisRepository analysisRepository;

    // 선택한 세션 불러오기
    public AnalysisResponse.Detail exec(Long analysisId, CustomUserDetails userDetails){
        // Analysis 조회
        User user = userHelper.validateUser(userDetails.getId());
        Analysis analysis = analysisRepository.findById(analysisId).orElseThrow(() -> new CustomException(ErrorCode.ANALYSIS_NOT_FOUND));

        // QuestionAnswer 조회
        List<QuestionAnswer> qaList = analysisHelper.getSortedQAList(analysis);

        // 평균 소요시간, 상위 % 계산
        double avgDurationPercent =
                analysisHelper.calculateAverageDurationPercent(user, analysis.getEnterprise(), analysis);
        double topScorePercent =
                analysisHelper.calculateTopScorePercent(analysis.getEnterprise(), analysis);

        // DTO 변환
        return analysisHelper.buildResponse(analysis, qaList, avgDurationPercent, topScorePercent);

    }
}
