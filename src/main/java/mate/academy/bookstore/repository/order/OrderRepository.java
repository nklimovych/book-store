package mate.academy.bookstore.repository.order;

import java.util.List;
import mate.academy.bookstore.model.order.Order;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"orderItems"})
    List<Order> findByUserId(Long userId);
}
