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
    @Column(name = "id", length = 36, nullable = false)
    private String id;

    @Column(name = "status", nullable = false)
    private boolean status;

    @Column(name = "result_score", nullable = false)
    private double resultScore;

    @Column(name = "follow_score", nullable = false)
    private double followScore;

    @Column(name = "result", length = 255, nullable = false)
    private String result;

    @Column(name = "keyword", length = 255, nullable = false)
    private String keyword;

    @Column(name = "keyword_number", length = 12, nullable = false)
    private String keywordNumber;

    @Column(name = "analysis_group", length = 36, nullable = false)
    private String analysisGroup;

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
