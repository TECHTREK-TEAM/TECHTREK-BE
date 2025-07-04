package techtrek.domain.basicQuestion.entity;

import jakarta.persistence.*;
import lombok.*;
import techtrek.domain.basicQuestion.entity.status.CsCategory;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="basic_question")
public class BasicQuestion {
    @Id
    @Column(name = "id", length = 36, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "question", nullable = false)
    private String question;

    @Column(name = "cs_category", nullable = false)
    @Enumerated(EnumType.STRING)
    private CsCategory csCategory;

    public BasicQuestion(String question, CsCategory csCategory) {
        this.question = question;
        this.csCategory = csCategory;
    }
}
