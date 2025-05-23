package techtrek.domain.sessionInfo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techtrek.domain.sessionInfo.entity.SessionInfo;

import java.util.Optional;

@Repository
public interface SessionInfoRepository extends JpaRepository<SessionInfo, String> {
    Optional<SessionInfo> findBySessionId(String sessionId);
}
