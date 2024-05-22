package mate.academy.bookstore.service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import mate.academy.bookstore.dto.order.OrderItemResponseDto;
import mate.academy.bookstore.dto.order.OrderRequestDto;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.dto.order.OrderStatusDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.order.Order;
import mate.academy.bookstore.model.order.OrderItem;

public interface OrderService {
    void createOrder(OrderRequestDto order, User user);

    List<OrderResponseDto> getAllOrders(User user);

    void updateStatus(OrderStatusDto statusDto, Long orderId, Long userId);

    Set<OrderItem> saveOrderItem(Order order, Map<Book, Integer> book);

    List<OrderItemResponseDto> getAllOrderItems(Long orderId);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId);
}

