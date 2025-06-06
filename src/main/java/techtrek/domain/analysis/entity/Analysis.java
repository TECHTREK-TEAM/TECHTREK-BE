package techtrek.domain.analysis.entity;

import jakarta.persistence.*;
import lombok.*;
import techtrek.domain.sessionInfo.entity.SessionInfo;

@Entity
@Table(name="analysis")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    private int duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_info_id")
    private SessionInfo sessionInfo;


}
