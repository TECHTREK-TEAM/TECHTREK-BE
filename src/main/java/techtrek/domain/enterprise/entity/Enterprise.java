package techtrek.domain.enterprise.entity;

import jakarta.persistence.*;

import lombok.*;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.interviewQuestion.entity.InterviewQuestion;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "enterprise")
public class Enterprise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "enterprise")
    private List<InterviewQuestion> interviewQuestions;

    @OneToMany(mappedBy = "enterprise")
    private List<Analysis> analyses;
}
