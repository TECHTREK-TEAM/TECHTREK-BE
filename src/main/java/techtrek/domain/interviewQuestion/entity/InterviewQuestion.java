package techtrek.domain.interviewQuestion.entity;

import jakarta.persistence.*;
import lombok.*;
import techtrek.domain.interviewQuestion.entity.status.Category;
import techtrek.domain.enterprise.entity.Enterprise;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="interview_question")
public class InterviewQuestion {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

//    public BasicQuestion(String question, Category category) {
//        this.question = question;
//        this.category = category;
//    }
}
