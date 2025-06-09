package techtrek.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import techtrek.domain.sessionInfo.entity.SessionInfo;
import techtrek.domain.stack.entity.Stack;

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

    @OneToMany(mappedBy = "user")
    private List<SessionInfo> sessionInfoList;

    @OneToMany(mappedBy = "user")
    private List<Stack> stackList;
}
