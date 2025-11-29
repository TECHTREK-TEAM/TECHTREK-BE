package techtrek.domain.basicQuestion.entity;

import jakarta.persistence.*;
import lombok.*;
import techtrek.domain.basicQuestion.entity.status.Category;
import techtrek.domain.enterprise.entity.Enterprise;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="basic_question")
public class BasicQuestion {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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

    public BasicQuestion(String question, String correctAnswer, Enterprise enterprise, String category) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.enterprise = enterprise;
        this.category = Category.valueOf(category);
    }

}
