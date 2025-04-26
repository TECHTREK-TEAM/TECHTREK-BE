package techtrek.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import techtrek.domain.sessionInfo.entity.SessionInfo;

@Entity
@Table(name="analysis")
@Getter
@Setter
public class Analysis {
    @Id
    private String id;

    @Column(nullable = false)
    private boolean expectation;

    @Column(nullable = false)
    private double matchRate;

    @Column(nullable = false)
    private int followUpHandling;


    @Column(length = 255, nullable = false)
    private String result;

    @Column(length = 12, nullable = false)
    private String keyword;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_info_id")
    private SessionInfo sessionInfo;


}
