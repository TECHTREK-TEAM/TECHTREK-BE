package techtrek.domain.stack.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.Id;
import techtrek.domain.user.entity.User;


@Entity
@Table(name="stack")
@Getter
@Setter
public class Stack {
    @Id
    @Column(length = 36, nullable = false)
    private String id;

    @Column(length = 12)
    private String stackName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
