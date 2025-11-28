package techtrek.domain.questionAnswer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.questionAnswer.entity.QuestionAnswer;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {

    // 최소 유사도 반환
    @Query("""
    SELECT q FROM QuestionAnswer q
    WHERE q.analysis.id = :analysisId
    ORDER BY q.similarity ASC
    LIMIT 1
""")
    Optional<QuestionAnswer> findLowestSimilarity(Long analysisId);

    // analysis 기준으로 모든 QuestionAnswer 조회
    List<QuestionAnswer> findByAnalysis(Analysis analysis);


}
