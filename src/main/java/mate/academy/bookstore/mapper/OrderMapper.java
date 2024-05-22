package mate.academy.bookstore.mapper;

import java.util.stream.Collectors;
import mate.academy.bookstore.config.MapperConfig;
import mate.academy.bookstore.dto.order.OrderResponseDto;
import mate.academy.bookstore.model.order.Order;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    OrderResponseDto toDto(Order order);

    @AfterMapping
    default void mapOrderItemsToDto(
            Order order,
            @MappingTarget OrderResponseDto responseDto,
            OrderItemMapper itemMapper
    ) {
        if (order.getOrderItems() != null) {
            responseDto.setOrderItems(order.getOrderItems().stream()
                                           .map(itemMapper::toDto)
                                           .collect(Collectors.toSet()));
        }
    }
}
