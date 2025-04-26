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
    private String id;

    @Column(nullable = false)
    private String name;
    private String userGroup;
    private String seniority;

    private String resume;

    @OneToMany(mappedBy = "user")
    private List<SessionInfo> sessionInfoList;

    @OneToMany(mappedBy = "user")
    private List<Stack> stackList;
}
