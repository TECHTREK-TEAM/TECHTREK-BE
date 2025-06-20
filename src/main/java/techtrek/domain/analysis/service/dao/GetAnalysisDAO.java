package techtrek.domain.analysis.service.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;

@Component
@RequiredArgsConstructor
public class GetAnalysisDAO {
    private final AnalysisRepository analysisRepository;

    // userId를 이용하여 합격 면접 수 조회
    public int exec(String userId){
        int interviewPass = analysisRepository.countByStatusTrueAndSessionInfoUserId(userId);
        return interviewPass;
    }
}
