package techtrek.domain.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import techtrek.domain.interview.entity.BasicQuestion;

import java.util.Optional;

@Repository
public interface BasicQuestionRepository extends JpaRepository<BasicQuestion, Integer> {
    // 랜덤으로 하나의 질문을 선택
    @Query(value = "SELECT * FROM basic_question ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Optional<BasicQuestion> findRandomQuestion();

}
