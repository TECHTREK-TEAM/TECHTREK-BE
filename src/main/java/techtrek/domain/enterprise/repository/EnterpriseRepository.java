package techtrek.domain.enterprise.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techtrek.domain.enterprise.entity.Enterprise;

import java.util.Optional;

@Repository
public interface EnterpriseRepository extends JpaRepository<Enterprise, Long> {
    // 이름으로 조회
    Optional<Enterprise> findByName(String name);
}
