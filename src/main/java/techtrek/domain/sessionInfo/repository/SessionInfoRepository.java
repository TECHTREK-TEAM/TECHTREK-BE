package techtrek.domain.sessionInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.user.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface SessionInfoRepository extends JpaRepository<SessionInfo, String> {
    // sessionId로 sessionInfo 찾기
    Optional<SessionInfo> findBySessionId(String sessionId);

    // 전체 면접 수
    @Query("SELECT COUNT(s) FROM SessionInfo s WHERE s.user.id = :userid")
    int countAllSessions(@Param("userid") String userid);

    // 전체 면접 조회
    @Query("SELECT s.id FROM SessionInfo s WHERE s.user.id = :userId")
    List<String> findSessionIdsByUserId(@Param("userId") String userId);

    // user로 enterpriseName 개수 계산
    @Query("SELECT s.enterpriseName AS enterpriseName, COUNT(s) AS cnt " +
            "FROM SessionInfo s WHERE s.user = :user GROUP BY s.enterpriseName")
    List<EnterpriseCount> countByEnterpriseName(@Param("user") User user);

    interface EnterpriseCount {
        String getEnterpriseName();
        Long getCnt();
    }
}
