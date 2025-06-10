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
    @Column(length = 36, nullable = false)
    private String id;

    @Column(nullable = false)
    private boolean status;

    @Column(nullable = false)
    private double resultScore;

    @Column(nullable = false)
    private double followScore;

    @Column(length = 255, nullable = false)
    private String result;

    @Column(length = 12, nullable = false)
    private String keyword;

    @Column(length = 12, nullable = false)
    private String keywordNumber;

    @Column(length = 36, nullable = false)
    private String analysisGroup;

    private int duration;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_info_id")
    private SessionInfo sessionInfo;


}
