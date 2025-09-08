package techtrek.domain.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import techtrek.domain.interview.entity.SessionInfo;
import techtrek.domain.basicQuestion.entity.status.EnterpriseName;
import techtrek.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionInfoRepository extends JpaRepository<SessionInfo, String> {
    // sessionId로 sessionInfo 조회
    Optional<SessionInfo> findBySessionId(String sessionId);

    // 사용자 기준으로 모든 SessionInfo 조회
    List<SessionInfo> findAllByUser(User user);

    // 전체 면접 수
    @Query("SELECT COUNT(s) FROM SessionInfo s WHERE s.user.id = :userid")
    int countAllSessions(@Param("userid") String userid);

    // 전체 sessionInfoId 조회
    @Query("SELECT s.id FROM SessionInfo s WHERE s.user.id = :userId")
    List<String> findSessionIdsByUserId(String userId);


    // enterpriseName, userId + 분석테이블에서 가장 최근 날짜 sessionInfoId 조회
    @Query("""
    SELECT s
    FROM SessionInfo s
    JOIN s.analysis a
    WHERE s.user.id = :userId AND s.enterpriseName = :enterpriseName
    ORDER BY a.createdAt DESC
    """)
    List<SessionInfo> findTopByUserIdAndEnterpriseNameOrderByAnalysisCreatedAtDesc(
            @Param("userId") String userId,
            @Param("enterpriseName") EnterpriseName enterpriseName
    );

}
