package mate.academy.bookstore.repository.user;

import java.util.Optional;
import mate.academy.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByEmail(String email);
}
