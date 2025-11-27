package techtrek.domain.questionAnswer.entity;

import jakarta.persistence.*;
import lombok.*;
import techtrek.domain.analysis.entity.Analysis;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name="question_answer")
public class QuestionAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "analysis_id", nullable = false)
    private Analysis analysis;

    @Column(nullable = false)
    private int mainNumber = 0;

    @Column
    private int subNunber = 0;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String question;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String correctAnswer;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false)
    private int similarity = 0;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
