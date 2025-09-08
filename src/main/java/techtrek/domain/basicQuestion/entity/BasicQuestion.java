package techtrek.domain.basicQuestion.entity;

import jakarta.persistence.*;
import lombok.*;
import techtrek.domain.basicQuestion.entity.status.Category;
import techtrek.domain.basicQuestion.entity.status.EnterpriseName;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="basic_question")
public class BasicQuestion {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "correct_answer", nullable = false)
    private String correctAnswer;

    @Enumerated(EnumType.STRING)
    @Column(name = "enterprise_name", nullable = false)
    private EnterpriseName enterpriseName;

    @Column(name = "category", nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

//    public BasicQuestion(String question, Category category) {
//        this.question = question;
//        this.category = category;
//    }
}
