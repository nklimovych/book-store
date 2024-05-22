package mate.academy.bookstore.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
import mate.academy.bookstore.model.Book;
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
        ShoppingCart cart = getCart(user);
        Map<Book, Integer> books = getBooks(cart);

        Order order = orderRepository.save(createNewOrder(orderDto, user, cart));
        Set<OrderItem> orderItems = saveOrderItem(order, books);
        order.setOrderItems(orderItems);
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
    public Set<OrderItem> saveOrderItem(Order order, Map<Book, Integer> book) {
        return book.entrySet().stream()
                   .map(e -> {
                       OrderItem item = new OrderItem();
                       item.setBook(e.getKey());
                       item.setQuantity(e.getValue());
                       item.setOrder(order);
                       item.setPrice(e.getKey().getPrice());
                       return item;
                   })
                   .map(itemRepository::save)
                   .collect(Collectors.toSet());
    }

    @Override
    public List<OrderItemResponseDto> getAllOrderItems(Long orderId) {
        List<OrderItem> items = itemRepository.findByOrderId(orderId);

        if (items.isEmpty()) {
            throw new EntityNotFoundException(
                    "Unable to proceed: No order items found for the order with id: " + orderId);
        }
        return items.stream()
                    .map(itemMapper::toDto)
                    .toList();
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new EntityNotFoundException(
                        "Unable to proceed: Order not found with id: " + orderId));

        return itemRepository.findByIdAndOrder(itemId, order)
                             .map(itemMapper::toDto)
                             .orElseThrow(() -> new EntityNotFoundException(
                                     "Unable to proceed: Order item not found with id: " + itemId));
    }

    private Order createNewOrder(OrderRequestDto orderDto, User user, ShoppingCart cart) {
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setStatus(Status.PENDING);
        newOrder.setTotal(getTotalPrice(cart));
        newOrder.setOrderDate(LocalDateTime.now());
        newOrder.setShippingAddress(orderDto.getShippingAddress());

        return newOrder;
    }

    private BigDecimal getTotalPrice(ShoppingCart cart) {
        return cart.getCartItems().stream()
                           .map(i -> i.getBook()
                                      .getPrice()
                                      .multiply(BigDecimal.valueOf(i.getQuantity())))
                           .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private ShoppingCart getCart(User user) {
        return cartRepository.findByUser_Id(user.getId())
                             .orElseGet(() -> cartRepository.save(new ShoppingCart(user)));
    }

    private Map<Book, Integer> getBooks(ShoppingCart cart) {
        return cart.getCartItems().stream()
                   .collect(Collectors.toMap(CartItem::getBook, CartItem::getQuantity));
    }
}
