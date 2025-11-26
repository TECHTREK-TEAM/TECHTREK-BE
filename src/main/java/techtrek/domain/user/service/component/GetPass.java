package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

@Component
@RequiredArgsConstructor
public class GetPass {
    private final UserValidator userValidator;
    private final AnalysisRepository analysisRepository;

    // 전체 합격률 조회
    public UserResponse.Pass exec(CustomUserDetails userDetails) {
        // 사용자 조회
        User user = userValidator.validateAndGetUser(userDetails.getId());

        // 전체 면접 수, 합격 면접 수 조회
        int interviewTotal = analysisRepository.countAllAnalysis(user.getId());
        int interviewPass = analysisRepository.countPassedAnalysis(user.getId());

        // 합격룰 계산
        double interviewPercent = 0.0;
        if (interviewTotal > 0) interviewPercent = Math.round(((double) interviewPass / interviewTotal) * 1000) / 10.0;

        return UserResponse.Pass.builder()
                .interviewTotal(interviewTotal)
                .interviewPass(interviewPass)
                .interviewPercent(interviewPercent)
                .build();
    }
}