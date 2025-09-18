package techtrek.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.stack.entity.Stack;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="user")
public class User {
    @Id
    @Column(name="id", length = 256, nullable = false)
    private String id;

    @Column(name="name", length = 36, nullable = false)
    private String name;

    @Column(name="position", length = 36)
    private String position;

    @Column(name = "email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "provider", length = 20, nullable = false)
    private String provider;

    @Column(name="seniority", length = 36)
    private String seniority;

    @Column(name="resume", length = 255)
    private String resume;

    @Column(name="resume_name", length = 255)
    private String resumeName;

    @Column(name = "role", length = 20, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Analysis> analysisList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stack> stackList;

}
