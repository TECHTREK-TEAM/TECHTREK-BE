package techtrek.domain.stack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import techtrek.domain.stack.entity.Stack;

@Repository
public interface StackRepository extends JpaRepository<Stack, String> {
    // 스택 삭제
    @Modifying
    @Transactional
    @Query("delete from Stack s where s.user.id = :userId")
    void deleteByUserId(String userId);
}
