package techtrek.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import techtrek.domain.analysis.entity.Analysis;
import techtrek.domain.stack.entity.Stack;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="user")
public class User {
    @Id
    @Column(name="id", length = 36, nullable = false)
    private String id;

    @Column(name="name", length = 36, nullable = false)
    private String name;

    @Column(name="user_group", length = 36)
    private String userGroup;

    @Column(name="seniority", length = 36)
    private String seniority;

    @Column(name="resume", length = 255)
    private String resume;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<Analysis> analysisList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Stack> stackList;


    // 변경 메서드
    public void changeUsername(String name) {
        if (name == null || name.isBlank()) throw new IllegalArgumentException("Username cannot be empty");
        this.name = name;
    }

    public void changeUserGroup(String userGroup) {
        this.userGroup = userGroup;
    }

    public void changeSeniority(String seniority) {
        this.seniority = seniority;
    }

    public void changeResume(String resume) {
        this.resume = resume;
    }

    // 스택 리스트 교체
    public void replaceStacks(List<Stack> newStacks) {
        this.stackList.clear();
        this.stackList.addAll(newStacks);
    }

    public void updateTimestamp() {
        this.updatedAt = LocalDateTime.now().withNano(0);
    }
}
