package techtrek.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.stack.entity.Stack;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="user")
public class User {
    @Id
    @Column(name="id", length = 36, nullable = false)
    private String id;

    @Column(name="name", length = 36, nullable = false)
    private String name;

    @Column(name="user_group", length = 36, nullable = true)
    private String userGroup;

    @Column(name="seniority", length = 36, nullable = true)
    private String seniority;

    @Column(name="resume", length = 255, nullable = true)
    private String resume;

    @Column(name = "updated_at", nullable = true)
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<SessionInfo> sessionInfoList;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stack> stackList;
}
