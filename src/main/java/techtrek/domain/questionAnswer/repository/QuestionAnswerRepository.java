package techtrek.domain.questionAnswer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    // 삭제
    @Modifying
    @Query("DELETE FROM QuestionAnswer q WHERE q.analysis = :analysis")
    void deleteByAnalysis(Analysis analysis);

    @Query("""
    SELECT qa
    FROM QuestionAnswer qa
    JOIN qa.analysis a
    JOIN a.user u
    WHERE u.id = :userId
      AND a.id = :analysisId
      AND qa.mainNumber = :mainNumber
      AND qa.subNumber = :subNumber
""")
    Optional<QuestionAnswer> findPreviousQa(
            @Param("userId") String userId,
            @Param("analysisId") Long analysisId,
            @Param("mainNumber") int mainNumber,
            @Param("subNumber") int subNumber
    );

    Optional<QuestionAnswer> findTopByAnalysisIdAndAnalysisUserIdOrderByMainNumberDescSubNumberDesc(Long analysisId, String userId);

}
