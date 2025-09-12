package techtrek.domain.stack.entity;

import jakarta.persistence.*;
import lombok.*;
import jakarta.persistence.Id;
import techtrek.domain.user.entity.User;


@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name="stack")
public class Stack {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", length = 36, nullable = false)
    private Long id;

    @Column(name="stack_name", length = 12, nullable = true)
    private String stackName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
}
