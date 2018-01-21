package restaurant.vote.system.rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import restaurant.vote.system.rest.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long userId);
    Optional<User> findByLogin(String login);
}
