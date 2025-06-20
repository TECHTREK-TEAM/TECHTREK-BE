package techtrek.domain.sessionInfo.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.sessionInfo.entity.status.EnterpriseName;
import techtrek.domain.user.entity.User;

import java.util.List;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="session_info")
public class SessionInfo {
    @Id
    @Column(name="id", length = 36, nullable = false)
    private String id;

    @Column(name="session_id", length = 36, nullable = false)
    private String sessionId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EnterpriseName enterpriseName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "sessionInfo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Analysis analysis;
}
