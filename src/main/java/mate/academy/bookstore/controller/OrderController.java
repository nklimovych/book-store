package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.order.OrderItemResponseDto;
import mate.academy.bookstore.dto.order.OrderRequestDto;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.dto.order.OrderStatusDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.OrderService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Order Management", description = "Endpoints for managing the order")
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "Create an order",
            description = "Creates a new order with the items in the current user cart")
    public void createOrder(
            @Valid @RequestBody OrderRequestDto orderDto,
            @AuthenticationPrincipal User user) {
        orderService.createOrder(orderDto, user);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "Get orders history",
            description = "Retrieves the order history for the current user")
    public List<OrderResponseDto> getOrdersHistory(
            @AuthenticationPrincipal User currentUser) {
        return orderService.getAllOrders(currentUser);
    }

    @PatchMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @Operation(summary = "Update an order status",
            description = "Updates the status of an existing order identified by its id")
    public void updateOrderStatus(
            @Valid @RequestBody OrderStatusDto statusDto,
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user) {
        orderService.updateStatus(statusDto, orderId, user.getId());
    }

    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "Get all order items from order",
            description = "Retrieves all items associated for authenticated user")
    public List<OrderItemResponseDto> getAllOrderItems(
            @PathVariable Long orderId,
            @AuthenticationPrincipal User user) {
        return orderService.getAllOrderItems(orderId, user);
    }

    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    @Operation(summary = "Get item from order",
            description = "Retrieves a specific item from an order identified by its id")
    public OrderItemResponseDto getOrderItem(
            @PathVariable Long orderId,
            @PathVariable Long itemId,
            @AuthenticationPrincipal User user) {
        return orderService.getOrderItem(orderId, itemId, user);
    }
}
