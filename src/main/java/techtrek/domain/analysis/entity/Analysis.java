package techtrek.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.*;
import techtrek.domain.sessionInfo.entity.SessionInfo;

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

    @Column(name = "keyword", length = 12, nullable = false)
    private String keyword;

    @Column(name = "keyword_number", length = 12, nullable = false)
    private String keywordNumber;

    @Column(name = "analysis_group", length = 36, nullable = false)
    private String analysisGroup;

    @Column(name = "duration", nullable = false)
    private int duration;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_info_id")
    private SessionInfo sessionInfo;


}
