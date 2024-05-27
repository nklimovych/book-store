package mate.academy.bookstore.repository.order;

import java.util.List;
import java.util.Optional;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT oi FROM OrderItem oi JOIN oi.order o WHERE o.id = :orderId AND o.user = :user")
    List<OrderItem> findByOrderIdAndUser(@Param("orderId") Long orderId, @Param("user") User user);

    @Query("SELECT oi FROM OrderItem oi LEFT JOIN oi.order o WHERE o.id = :orderId "
            + "AND oi.id = :itemId AND o.user = :user")
    Optional<OrderItem> findByOrderIdAndItemIdAndUser(
            @Param("orderId") Long orderId, @Param("itemId") Long itemId, @Param("user") User user);
}
