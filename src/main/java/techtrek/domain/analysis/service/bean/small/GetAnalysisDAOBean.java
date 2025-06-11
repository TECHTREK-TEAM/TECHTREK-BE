package techtrek.domain.analysis.service.bean.small;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;

@Component
@RequiredArgsConstructor
public class GetAnalysisDAOBean {
    private final AnalysisRepository analysisRepository;

    // userId를 이용하여 합격 면접 수 조회
    public int exec(String userId){
        int interviewPass = analysisRepository.countByStatusTrueAndSessionInfoUserId(userId);
        return interviewPass;
    }
}
