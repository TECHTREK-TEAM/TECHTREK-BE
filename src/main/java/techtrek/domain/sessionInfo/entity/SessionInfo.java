package techtrek.domain.sessionInfo.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.sessionInfo.entity.status.EnterpriseType;
import techtrek.domain.user.entity.User;

import java.util.List;

@Entity
@Table(name="session_info")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SessionInfo {
    @Id
    private String id;

    @Column(length = 255, nullable = false)
    private String sessionId;

    @Column(length = 12, nullable = false)
    private String enterpriseName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnterpriseType enterpriseType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "sessionInfo")
    private List<Analysis> analysisList;
}
