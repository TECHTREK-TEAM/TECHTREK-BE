package techtrek.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import techtrek.domain.enterprise.entity.Enterprise;

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
    @JoinColumn(name = "enterprise_id")
    private Enterprise enterprise;


}
