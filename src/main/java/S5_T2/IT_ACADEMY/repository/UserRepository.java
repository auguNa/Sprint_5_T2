package S5_T2.IT_ACADEMY.repository;

import S5_T2.IT_ACADEMY.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}