package techtrek.domain.analysisQA;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InterviewRecordRepository extends JpaRepository<InterviewRecord, Long> {

    @Query("SELECT q FROM InterviewRecord q " +
            "JOIN FETCH q.interviewSession s " +
            "JOIN FETCH s.analysis a " +
            "WHERE s.id = :sessionId " +
            "ORDER BY q.questionNumber ASC")
    List<InterviewRecord> findAllByInterviewSessionIdOrderByQuestionNumberAsc(@Param("sessionId") Long sessionId);

}

