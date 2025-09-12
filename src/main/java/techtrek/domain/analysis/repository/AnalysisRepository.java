package techtrek.domain.analysis.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, String> {
    // 사용자와 기업 객체 기준으로 분석 데이터 조회
    List<Analysis> findByUserAndEnterpriseOrderByCreatedAtDesc(User user, Enterprise enterprise);

    // 사용자와 기업 객체 기준으로 최신 분석 데이터 조회
    Optional<Analysis> findTopByUserAndEnterpriseOrderByCreatedAtDesc(User user, Enterprise enterprise);

    // 특정 Enterprise에 속한 모든 분석 결과 조회
    List<Analysis> findAllByEnterprise(Enterprise enterprise);

    // 관심 기업 3
    @Query("""
        SELECT a.enterprise.name AS name,
               AVG(a.score) AS avgScore
        FROM Analysis a
        WHERE a.user = :user
        GROUP BY a.enterprise
        ORDER BY COUNT(a) DESC
    """)
    List<TopCompany> findTopEnterprisesByUser(User user, Pageable pageable);

    // 전체 면접 수
    @Query("SELECT COUNT(s) FROM Analysis s WHERE s.user.id = :userid")
    int countAllAnalysis(String userid);

    // 합격 면접 수
    @Query("SELECT COUNT(a) FROM Analysis a WHERE a.user.id = :userId AND a.isPass = true")
    int countPassedAnalysis(String userId);

    // 전체 일치율 평균
    @Query("SELECT AVG(a.score) FROM Analysis a WHERE a.user.id = :userId")
    Double findAvgScoreByUser(String userId);

    // 이번 달, 저번 달 평균
    @Query("""
    SELECT AVG(a.score)
    FROM Analysis a
    WHERE a.user.id = :userId
      AND YEAR(a.createdAt) = :year
      AND MONTH(a.createdAt) = :month
    """)
    Double findAvgScoreByUserAndMonth(String userId, int year, int month);

    // 점수 최고 분석
    @Query("""
        SELECT a 
        FROM Analysis a 
        WHERE a.user.id = :userId 
        ORDER BY a.score DESC
    """)
    List<Analysis> findTopByUserOrderByScoreDesc(String userId, Pageable pageable);

    // 최근 분석
    @Query("""
        SELECT a 
        FROM Analysis a 
        WHERE a.user.id = :userId 
        ORDER BY a.createdAt DESC
    """)
    List<Analysis> findTopByUserOrderByCreatedAtDesc( String userId, Pageable pageable);


}
