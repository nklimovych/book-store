package mate.academy.bookstore.repository.role;

import java.util.Set;
import mate.academy.bookstore.model.Role;
import mate.academy.bookstore.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("SELECT r FROM Role r WHERE r.role IN :roles")
    Set<Role> findAllByRoles(Set<RoleName> roles);
}
