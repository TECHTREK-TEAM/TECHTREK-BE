package techtrek.domain.user.service.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.user.dto.UserResponse;
import techtrek.domain.user.entity.User;

@Component
@RequiredArgsConstructor
public class GetInterviewDTO {

    public UserResponse.Interview exec(User user, Analysis highestScoreAnalysis, Analysis recentAnalysis) {

        // 가장 점수 높은 면접 정보 구성
        UserResponse.Interview.InterviewData highestScore = null;
        if (highestScoreAnalysis != null) {
            SessionInfo si = highestScoreAnalysis.getSessionInfo();
            highestScore = UserResponse.Interview.InterviewData.builder()
                    .status(highestScoreAnalysis.isStatus())
                    .enterpriseName(si.getEnterpriseName())
                    .resultScore(highestScoreAnalysis.getResultScore())
                    .analysisGroup(highestScoreAnalysis.getAnalysisGroup())
                    .build();
        }

        // 가장 최근 면접 정보 구성
        UserResponse.Interview.InterviewData mostRecent = null;
        if (recentAnalysis != null) {
            SessionInfo si = recentAnalysis.getSessionInfo();
            mostRecent = UserResponse.Interview.InterviewData.builder()
                    .status(recentAnalysis.isStatus())
                    .enterpriseName(si.getEnterpriseName())
                    .resultScore(recentAnalysis.getResultScore())
                    .analysisGroup(recentAnalysis.getAnalysisGroup())
                    .build();
        }

        // 이력서 상태
        UserResponse.Interview.Resume resume = UserResponse.Interview.Resume.builder()
                .status(user.getResume() != null && !user.getResume().isEmpty())
                .build();

        // 최종 DTO 반환
        return UserResponse.Interview.builder()
                .highestScore(highestScore)
                .recentInterview(mostRecent)
                .resume(resume)
                .build();
    }
}
