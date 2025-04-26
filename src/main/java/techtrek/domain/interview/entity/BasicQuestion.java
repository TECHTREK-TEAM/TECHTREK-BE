package techtrek.domain.interview.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import techtrek.domain.interview.entity.status.QuestionCategory;

@Entity
@Table(name="basic_question")
@Getter
@Setter
public class BasicQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionCategory questionCategory;
}
