package techtrek.domain.basicQuestion.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import techtrek.domain.basicQuestion.entity.status.CsCategory;

@Entity
@Table(name="basic_question")
@Getter
@Setter
public class BasicQuestion {
    @Id
    @Column(length = 36, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String question;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CsCategory csCategory;
}
