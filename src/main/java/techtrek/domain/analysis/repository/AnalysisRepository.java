package techtrek.domain.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.interviewQuestion.entity.status.EnterpriseName;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, String> {
//    // 점수가 가장 높은 면접
//    Optional<Analysis> findTopBySessionInfoIdInOrderByResultScoreDesc(List<String> sessionIds);
//
//    // 가장 최근 면접
//    Optional<Analysis> findTopBySessionInfoIdInOrderByCreatedAtDesc(List<String> sessionIds);
//
//    // 일치율 전체 평균
//    @Query("SELECT AVG(a.resultScore) FROM Analysis a WHERE a.sessionInfo.id IN :sessionIds")
//    Double findAverageScoreBySessionIds(List<String> sessionIds);
//
//    // 일치율 해당 달 평균
//    @Query("SELECT AVG(a.resultScore) FROM Analysis a " +
//            "WHERE a.sessionInfo.id IN :sessionIds AND a.createdAt BETWEEN :start AND :end")
//    Double findAverageScoreBySessionIdsAndDateRange(
//            List<String> sessionIds, LocalDateTime start, LocalDateTime end
//    );
//
//    // 합격 면접 수 (status = true)
//    int countByStatusTrueAndSessionInfoUserId(String userId);
//
//    // 평균 연계질문 점수
//    @Query("""
//    SELECT AVG(a.followScore)
//    FROM Analysis a
//    WHERE a.sessionInfo.user.id = :userId
//      AND a.sessionInfo.enterpriseName = :enterpriseName
//""")
//    double getAverageFollowScoreByUserAndEnterprise(String userId, EnterpriseName enterpriseName);
//    // 평균 시간
//    @Query("""
//    SELECT AVG(a.duration)
//    FROM Analysis a
//    WHERE a.sessionInfo.user.id = :userId
//      AND a.sessionInfo.enterpriseName = :enterpriseName
//""")
//    double getAverageDurationByUserAndEnterprise(String userId, EnterpriseName enterpriseName);
//
//    // 내가 속한 enterpriseName 전체 score 수
//    @Query("SELECT COUNT(a) FROM Analysis a WHERE a.sessionInfo.enterpriseName = :enterpriseName")
//    long countByEnterprise(EnterpriseName enterpriseName);
//
//    // 나보다 낮은 점수 수
//    @Query("SELECT COUNT(a) FROM Analysis a WHERE a.sessionInfo.enterpriseName = :enterpriseName AND a.resultScore < :resultScore")
//    long countLowerScoreInEnterprise(EnterpriseName enterpriseName, double resultScore);
//

}
