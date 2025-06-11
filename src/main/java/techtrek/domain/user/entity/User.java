package techtrek.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.stack.entity.Stack;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="user")
@Getter
@Setter
public class User {
    @Id
    @Column(length = 36, nullable = false)
    private String id;

    @Column(length = 36, nullable = false)
    private String name;

    @Column(length = 36, nullable = false)
    private String userGroup;

    @Column(length = 36, nullable = false)
    private String seniority;

    @Column(length = 255)
    private String resume;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<SessionInfo> sessionInfoList;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stack> stackList;
}
