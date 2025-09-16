package techtrek.domain.enterprise.entity;

import jakarta.persistence.*;

import lombok.*;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.interviewQuestion.entity.InterviewQuestion;

import java.util.ArrayList;
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
    private Long id;

    private String name;

    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InterviewQuestion> interviewQuestions = new ArrayList<>();

    @OneToMany(mappedBy = "enterprise", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Analysis> analyses = new ArrayList<>();
}
