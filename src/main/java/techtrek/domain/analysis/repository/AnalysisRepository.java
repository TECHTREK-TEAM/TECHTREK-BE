package techtrek.domain.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import techtrek.domain.analysis.entity.Analysis;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, String> {
    // 합격 면접 수 (status = true)
    int countByStatusTrueAndSessionInfoUserId(String userId);

    // 일치율 전체 평균
    @Query("SELECT AVG(a.resultScore) FROM Analysis a WHERE a.sessionInfo.id IN :sessionIds")
    Double findAverageScoreBySessionIds(@Param("sessionIds") List<String> sessionIds);

    // 일치율 해당 달 평균
    @Query("SELECT AVG(a.resultScore) FROM Analysis a " +
            "WHERE a.sessionInfo.id IN :sessionIds AND a.createdAt BETWEEN :start AND :end")
    Double findAverageScoreBySessionIdsAndDateRange(
            @Param("sessionIds") List<String> sessionIds,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end
    );

}
