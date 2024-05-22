package mate.academy.bookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;

@Data
public class OrderResponseDto {
    Long id;
    Long userId;
    Set<OrderItemResponseDto> orderItems;
    LocalDateTime orderDate;
    BigDecimal total;
    String status;
}
