package techtrek.domain.user.service.component;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.repository.UserRepository;
import techtrek.global.common.code.ErrorCode;
import techtrek.global.common.exception.CustomException;
import techtrek.global.securty.service.CustomUserDetails;
import techtrek.global.securty.service.UserValidator;

@Component
@RequiredArgsConstructor
public class GetInterview {
    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final AnalysisRepository analysisRepository;

    // 면접 정보(높은점수, 최근) 조회
    public UserResponse.Interview exec(CustomUserDetails userDetails) {
        // 사용자 조회
        User user = userValidator.validateAndGetUser(userDetails.getId());

        // 최고 점수 Analysis
        Pageable one = PageRequest.of(0, 1);
        Analysis highest = analysisRepository.findTopByUserOrderByScoreDesc(user.getId(), one)
                .stream().findFirst().orElse(null);
        Analysis recent = analysisRepository.findTopByUserOrderByCreatedAtDesc(user.getId(), one)
                .stream().findFirst().orElse(null);

        return UserResponse.Interview.builder()
                .highestScore(highest != null ? UserResponse.Interview.InterviewData.builder()
                        .analysisId(highest.getId())
                        .isPass(highest.isPass())
                        .enterpriseName(highest.getEnterprise().getName())
                        .score(highest.getScore())
                        .analysisPosition(highest.getAnalysisPosition())
                        .build()
                        : null) // 없으면 null
                .recentInterview(recent != null ? UserResponse.Interview.InterviewData.builder()
                        .analysisId(recent.getId())
                        .isPass(recent.isPass())
                        .enterpriseName(recent.getEnterprise().getName())
                        .score(recent.getScore())
                        .analysisPosition(recent.getAnalysisPosition())
                        .build()
                        : null)
                .resume(UserResponse.Interview.Resume.builder()
                        .status(user.getResume() != null)
                        .resumeName(user.getResumeName())
                        .build())
                .build();
    }
}
