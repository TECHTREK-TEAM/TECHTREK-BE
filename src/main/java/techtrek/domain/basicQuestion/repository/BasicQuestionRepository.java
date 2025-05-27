package techtrek.domain.basicQuestion.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techtrek.domain.basicQuestion.entity.BasicQuestion;
import techtrek.domain.basicQuestion.entity.status.CSCategory;

import java.util.List;

@Repository
public interface BasicQuestionRepository extends JpaRepository<BasicQuestion, Integer> {

    // 랜점으로 하나의 질문 선택
    List<BasicQuestion> findByCSCategory(CSCategory csCategory);


}
