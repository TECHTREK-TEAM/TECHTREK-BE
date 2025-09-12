package techtrek.domain.analysis.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.enterprise.repository.EnterpriseRepository;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetAnalysisList {
    private final UserRepository userRepository;
    private final EnterpriseRepository enterpriseRepository;
    private final AnalysisRepository analysisRepository;

    // 세션 리스트 불러오기
    public AnalysisResponse.AnalysisList exec(String enterpriseName){
        // TODO: 사용자 조회
        User user = userRepository.findById("1").orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        // 기업 조회
        Enterprise enterprise = enterpriseRepository.findByName(enterpriseName).orElseThrow(() -> new CustomException(ErrorCode.ENTERPRISE_NOT_FOUND));

        // 해당 기업의 세션정보 list 조회 (내림차순)
        List<Analysis> analyses = analysisRepository.findByUserAndEnterpriseOrderByCreatedAtDesc(user, enterprise);

        // DTO 변환
        List<AnalysisResponse.AnalysisList.Data> sessionInfos = analyses.stream()
                .map(analysis -> AnalysisResponse.AnalysisList.Data.builder()
                        .enterpriseName(analysis.getEnterprise().getName())
                        .createdAt(analysis.getCreatedAt())
                        .build())
                .collect(Collectors.toList());

        return AnalysisResponse.AnalysisList.builder()
                .sessions(sessionInfos)
                .build();
    }
}
