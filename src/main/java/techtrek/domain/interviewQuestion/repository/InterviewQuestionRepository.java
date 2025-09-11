package techtrek.domain.interviewQuestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import techtrek.domain.interviewQuestion.entity.InterviewQuestion;

import java.util.Optional;

@Repository
public interface InterviewQuestionRepository extends JpaRepository<InterviewQuestion, Integer> {
    boolean existsByQuestion(String question);

    // 특정 기업의 질문 중 랜덤 1개 가져오기
    @Query(value = "SELECT * FROM interview_question WHERE enterprise_id = :enterpriseId ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<InterviewQuestion> findRandomQuestionByEnterpriseId(@Param("enterpriseId") int enterpriseId);

}
