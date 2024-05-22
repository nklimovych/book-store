package mate.academy.bookstore.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.order.Order;
import mate.academy.bookstore.model.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    Optional<OrderItem> findByIdAndOrder(Long itemId, Order order);
}
