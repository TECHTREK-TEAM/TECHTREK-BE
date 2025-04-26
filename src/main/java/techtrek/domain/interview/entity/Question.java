package techtrek.domain.interview.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import techtrek.domain.interview.entity.status.QuestionCategory;

@Entity
@Table(name="Question")
@Getter
@Setter
public class Question {
    @Id
    private String id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private QuestionCategory questionCategory;
}
