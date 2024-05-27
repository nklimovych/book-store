package mate.academy.bookstore.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderItemResponseDto;
import mate.academy.bookstore.dto.order.OrderRequestDto;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.dto.order.OrderStatusDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.model.order.Order;
import mate.academy.bookstore.model.order.OrderItem;
import mate.academy.bookstore.model.order.Status;
import mate.academy.bookstore.repository.cart.ShoppingCartRepository;
import mate.academy.bookstore.repository.order.OrderItemRepository;
import mate.academy.bookstore.repository.order.OrderRepository;
import mate.academy.bookstore.service.OrderService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository itemRepository;
    private final OrderItemMapper itemMapper;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public void createOrder(OrderRequestDto orderDto, User user) {
        ShoppingCart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() ->
                new EntityNotFoundException("The authorized user does not have a shopping cart"));

        Set<CartItem> items = cart.getCartItems();
        if (items.isEmpty()) {
            throw new EntityNotFoundException("Unable to proceed: Cart is empty");
        }

        Order order = createNewOrder(orderDto, user, cart);
        Set<OrderItem> orderItems = createOrderItems(order, items);
        order.setOrderItems(orderItems);

        cartRepository.delete(cart);
        orderRepository.save(order);
    }

    @Transactional
    @Override
    public List<OrderResponseDto> getAllOrders(User currentUser) {
        return orderRepository.findByUserId(currentUser.getId()).stream()
                              .map(orderMapper::toDto)
                              .toList();
    }

    @Transactional
    @Override
    public void updateStatus(OrderStatusDto statusDto, Long orderId, Long userId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException(
                        "Unable to proceed: Order not found with id: " + orderId));
        order.setStatus(statusDto.getStatus());
        orderRepository.save(order);
    }

    @Override
    public List<OrderItemResponseDto> getAllOrderItems(Long orderId, User user) {
        List<OrderItem> items = itemRepository.findByOrderIdAndUser(orderId, user);

        if (items.isEmpty()) {
            throw new EntityNotFoundException(
                    "Unable to proceed: No order items found for user with id: " + user.getId());
        }
        return items.stream()
                    .map(itemMapper::toDto)
                    .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long itemId, User user) {
        return itemRepository.findByOrderIdAndItemIdAndUser(orderId, itemId, user)
                         .map(itemMapper::toDto)
                         .orElseThrow(() -> new EntityNotFoundException(
                                 "Unable to proceed: The requested item with id " + itemId
                                         + " was not found in order with id " + orderId));
    }

    private Set<OrderItem> createOrderItems(Order order, Set<CartItem> cartItems) {
        return cartItems.stream()
                        .map(cartItem -> {
                            OrderItem item = new OrderItem();
                            item.setBook(cartItem.getBook());
                            item.setQuantity(cartItem.getQuantity());
                            item.setOrder(order);
                            item.setPrice(cartItem.getBook().getPrice());
                            return itemRepository.save(item);
                        })
                        .collect(Collectors.toSet());
    }

    private Order createNewOrder(OrderRequestDto orderDto, User user, ShoppingCart cart) {
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setTotal(calculateTotalPrice(cart));
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(orderDto.getShippingAddress());

        return orderRepository.save(order);
    }

    private BigDecimal calculateTotalPrice(ShoppingCart cart) {
        return cart.getCartItems().stream()
                   .map(i -> i.getBook()
                              .getPrice()
                              .multiply(BigDecimal.valueOf(i.getQuantity())))
                   .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
