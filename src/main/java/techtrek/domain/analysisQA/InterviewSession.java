package techtrek.domain.analysisQA;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.user.entity.User;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class InterviewSession {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "interviewSession", cascade = CascadeType.ALL)
    private List<InterviewRecord> qaList;

    @OneToOne(mappedBy = "interviewSession", cascade = CascadeType.ALL)
    private Analysis analysis;
}
