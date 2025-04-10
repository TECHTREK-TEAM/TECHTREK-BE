package techtrek.domain.Question.Entity;

import jakarta.persistence.*;
import jdk.jfr.Category;
import lombok.Getter;
import lombok.Setter;
import techtrek.domain.Question.Entity.status.QuestionCategory;

@Entity
@Table(name="question")
@Getter
@Setter
public class question {
    @Id
    private String id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionCategory questionCategory;
}
