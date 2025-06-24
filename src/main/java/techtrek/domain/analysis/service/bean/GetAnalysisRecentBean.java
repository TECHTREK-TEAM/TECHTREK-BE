package techtrek.domain.analysis.service.bean;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import techtrek.domain.analysis.dto.AnalysisResponse;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.analysis.service.small.CreateAnalysisDetailDTO;
import techtrek.domain.analysis.service.small.GetAverageFollowScoreDAO;
import techtrek.domain.analysis.service.small.GetAvgDurationDAO;
import techtrek.domain.analysis.service.small.GetEnterpriseAnalysisCountDAO;
import techtrek.domain.redis.service.common.GetRedisHashUtil;
import techtrek.domain.sessionInfo.dto.SessionParserResponse;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.sessionInfo.service.small.GetSessionInfoListDAO;
import techtrek.domain.user.entity.User;
import techtrek.domain.user.service.small.GetUserDAO;
import techtrek.domain.redis.service.small.GetRedisByKeyDAO;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GetAnalysisRecentBean {

    private final GetRedisHashUtil getRedisHashUtil;

    private final GetUserDAO getUserDAO;
    private final GetRedisByKeyDAO getRedisByKeyDAO;
    private final GetSessionInfoListDAO getSessionInfoListDAO;
    private final GetAvgDurationDAO getAvgDurationDAO;
    private final GetAverageFollowScoreDAO getAverageFollowScoreDAO;
    private final GetEnterpriseAnalysisCountDAO getEnterpriseAnalysisCountDAO;
    private final CreateAnalysisDetailDTO createAnalysisDetailDTO;

    // 최근 세션 불러오기
    public AnalysisResponse.Detail exec(EnterpriseName enterpriseName){
        // TODO:사용자 조회
        User user = getUserDAO.exec("1");

        // 해당 기업의 세션정보 list 조회 (내림차순)
        List<SessionInfo> sessionInfos = getSessionInfoListDAO.exec(user.getId(), enterpriseName);
        SessionInfo sessionInfo = sessionInfos.get(0);
        Analysis analysis = sessionInfo.getAnalysis();

        // 분석 데이터 수치
        double followScore = analysis.getFollowScore();
        int duration = analysis.getDuration();
        double resultScore = analysis.getResultScore();

        // 전체 평균 데이터
        double avgFollowScore = getAverageFollowScoreDAO.exec();
        double avgDuration = getAvgDurationDAO.exec();

        // 퍼센트 차이 계산
        double followScoreDiffPercent = ((followScore - avgFollowScore) / avgFollowScore) * 100;
        followScoreDiffPercent = Math.round(followScoreDiffPercent * 10) / 10.0;
        double durationDiffPercent = ((duration - avgDuration) / avgDuration) * 100;
        durationDiffPercent = Math.round(durationDiffPercent * 10) / 10.0;

        // 상위 점수 비율 계산
        long totalCount = getEnterpriseAnalysisCountDAO.exec(enterpriseName);
        long lowerCount = getEnterpriseAnalysisCountDAO.exec(enterpriseName, resultScore);
        double topScorePercent = ((double)(lowerCount + 1) / totalCount) * 100;

        // 모든 hash 데이터 조회
        Set<String> allKeys = new HashSet<>();
        allKeys.addAll(getRedisHashUtil.exec("interview:session:" + sessionInfo.getSessionId() + "*"));
        List<SessionParserResponse.ListData> listData = getRedisByKeyDAO.exec(allKeys);

        return createAnalysisDetailDTO.exec(sessionInfo, analysis, followScoreDiffPercent, durationDiffPercent, topScorePercent, listData);

    }
}