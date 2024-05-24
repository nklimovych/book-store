package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.order.OrderItemResponseDto;
import mate.academy.bookstore.dto.order.OrderRequestDto;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.dto.order.OrderStatusDto;
import mate.academy.bookstore.model.User;

public interface OrderService {
    void createOrder(OrderRequestDto order, User user);

    List<OrderResponseDto> getAllOrders(User user);

    void updateStatus(OrderStatusDto statusDto, Long orderId, Long userId);

    List<OrderItemResponseDto> getAllOrderItems(Long orderId);

    OrderItemResponseDto getOrderItem(Long orderId, Long itemId, User user);
}
