package mate.academy.bookstore.repository.cart;

import java.util.Optional;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("FROM ShoppingCart sc JOIN FETCH sc.cartItems WHERE sc.user = :user")
    Optional<ShoppingCart> findByUser(User user);

    Optional<ShoppingCart> findByUserId(Long userId);
}
