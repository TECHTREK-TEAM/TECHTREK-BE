package techtrek.domain.stack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import techtrek.domain.stack.entity.Stack;

@Repository
public interface StackRepository extends JpaRepository<Stack, String> {

}
