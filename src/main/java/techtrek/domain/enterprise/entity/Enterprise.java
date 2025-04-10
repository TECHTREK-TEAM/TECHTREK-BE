package techtrek.domain.enterprise.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.enterprise.entity.status.EnterpriseType;
import techtrek.domain.user.entity.User;

import java.util.List;

@Entity
@Table(name="enterprise")
@Getter
@Setter
public class Enterprise {
    @Id
    private String id;

    @Column(length = 12, nullable = false)
    private String enterpriseName;

    @Column(length = 255, nullable = false)
    private String enterpriseTendency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnterpriseType enterpriseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "enterprise")
    private List<Analysis> analysisList;
}
