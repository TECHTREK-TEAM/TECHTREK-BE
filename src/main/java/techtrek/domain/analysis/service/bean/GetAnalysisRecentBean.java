package techtrek.domain.analysis.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.repository.AnalysisRepository;
import techtrek.domain.sessionInfo.dto.SessionParserResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.repository.SessionInfoRepository;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.dao.GetUserDAO;
import techtrek.domain.redis.service.small.GetRedisByKeyDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GetAnalysisRecentBean {

    private final GetUserDAO getUserDAO;
    private final GetRedisByKeyDAO redisDataByKeysDAOBean;
    private final SessionInfoRepository sessionInfoRepository;
    private final AnalysisRepository analysisRepository;
    private final RedisTemplate<String, String> redisTemplate;

    // 세션 불러오기
    public AnalysisResponse.Detail exec(EnterpriseName enterpriseName){
        // 사용자 조회
        User user = getUserDAO.exec("1");

        // 세션정보 list 조회
        List<SessionInfo> sessionInfos = sessionInfoRepository
                .findTopByUserIdAndEnterpriseNameOrderByAnalysisCreatedAtDesc(user.getId(), enterpriseName);

        // 사용자의 분석 데이터
        SessionInfo sessionInfo = sessionInfos.get(0);
        String sessionInfoId = sessionInfo.getId();
        String sessionId = sessionInfo.getSessionId();
        Analysis analysis = sessionInfo.getAnalysis();

        double userFollowScore = analysis.getFollowScore();
        int userDuration = analysis.getDuration();

        // 전체 평균 가져오기
        double avgFollowScore = analysisRepository.getAverageFollowScore();
        double avgDuration = analysisRepository.getAverageDuration();

        // 퍼센트 차이 계산
        double followScoreDiffPercent = ((userFollowScore - avgFollowScore) / avgFollowScore) * 100;
        double durationDiffPercent = ((userDuration - avgDuration) / avgDuration) * 100;

        double resultScore = analysis.getResultScore();

        // 전체 수
        long total = analysisRepository.countByEnterprise(enterpriseName);

        // 나보다 낮은 사람 수
        long lower = analysisRepository.countLowerScoreInEnterprise(enterpriseName, resultScore);

        // 상위 퍼센트 계산
        double topScorePercent = ((double)(lower + 1) / total) * 100;

        // dto
        AnalysisResponse.Detail.Analysis analysisResult = AnalysisResponse.Detail.Analysis.builder()
                .followScore(userFollowScore)
                .averageFollowPercent(followScoreDiffPercent)
                .duration(userDuration)
                .averageDurationPercent(durationDiffPercent)
                .resultScore(analysis.getResultScore())
                .topScore(topScorePercent)
                .status(analysis.isStatus())
                .build();

        String pattern = "interview:session:" + sessionId + ":*";
        Set<String> keys = redisTemplate.keys(pattern);

        List<SessionParserResponse.ListData> listData = redisDataByKeysDAOBean.exec( keys);


        List<AnalysisResponse.Detail.Interview> interviewList = new ArrayList<>();
        for (SessionParserResponse.ListData data : listData) {
            AnalysisResponse.Detail.Interview interview = AnalysisResponse.Detail.Interview.builder()
                    .question(data.getQuestion())
                    .answer(data.getAnswer())
                    .questionNumber(data.getQuestionNumber())
                    .build();
            interviewList.add(interview);
        }

        AnalysisResponse.Detail.Feedback feedback = AnalysisResponse.Detail.Feedback.builder()
                .keyword(analysis.getKeyword())
                .keywordNumber(analysis.getKeywordNumber())
                .result(analysis.getResult())
                .build();

        return new AnalysisResponse.Detail(sessionInfoId,analysisResult,interviewList,feedback);

    }
}