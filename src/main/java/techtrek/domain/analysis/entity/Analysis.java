package techtrek.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.*;
import techtrek.domain.enterprise.entity.Enterprise;
import techtrek.domain.user.entity.User;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="analysis")
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", length = 36, nullable = false)
    private Long id;

    @Column(name = "session_id", length = 255, nullable = false)
    private String sessionId;

    @Column(name = "is_pass", nullable = false)
    private boolean isPass;

    @Column(name = "score", nullable = false)
    private double score;

    @Column(name = "keyword", length = 255, nullable = false)
    private String keyword;

    @Column(name = "keyword_number", length = 12, nullable = false)
    private String keywordNumber;

    @Column(name = "feedback", length = 255, nullable = false)
    private String feedback;

    @Column(name = "analysis_role", length = 36, nullable = false)
    private String analysisRole;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enterprise_id", nullable = false)
    private Enterprise enterprise;


}
