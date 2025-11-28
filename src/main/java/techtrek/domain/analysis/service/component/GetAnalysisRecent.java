package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.analysis.service.helper.AnalysisHelper;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.questionAnswer.entity.QuestionAnswer;
import techtrek.domain.user.service.helper.UserHelper;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.domain.user.entity.User;
import techtrek.global.securty.service.CustomUserDetails;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAnalysisRecent {
    private final AnalysisHelper analysisHelper;
    private final UserHelper userHelper;
    private final EnterpriseRepository enterpriseRepository;
    private final AnalysisRepository analysisRepository;

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
        List<QuestionAnswer> qaList = analysisHelper.getSortedQAList(latestAnalysis);

        // 평균 소요시간, 상위 % 계산
        double avgDurationPercent =
                analysisHelper.calculateAverageDurationPercent(user, enterprise, latestAnalysis);
        double topScorePercent =
                analysisHelper.calculateTopScorePercent(enterprise, latestAnalysis);

        // DTO 변환
        return analysisHelper.buildResponse(latestAnalysis, qaList, avgDurationPercent, topScorePercent);

    }

}