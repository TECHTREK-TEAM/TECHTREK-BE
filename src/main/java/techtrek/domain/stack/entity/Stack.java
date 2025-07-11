package techtrek.domain.stack.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;
import techtrek.domain.user.entity.User;


@Entity
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="stack")
public class Stack {
    @Id
    @Column(name="id", length = 36, nullable = false)
    private String id;

    @Column(name="stack_name", length = 12, nullable = true)
    private String stackName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
