package techtrek.domain.analysis.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techtrek.domain.analysis.entity.Analysis;

@Repository
public interface AnalysisRepository extends JpaRepository<Analysis, String> {

}
