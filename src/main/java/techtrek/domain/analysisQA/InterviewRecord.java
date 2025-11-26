package techtrek.domain.analysisQA;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class InterviewRecord {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "interview_session_id")
    private InterviewSession interviewSession;

    private String question;
    private String answer;
    private String questionNumber;
}