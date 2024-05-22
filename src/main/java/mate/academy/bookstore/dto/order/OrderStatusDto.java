package mate.academy.bookstore.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import mate.academy.bookstore.model.order.Status;

@Data
public class OrderStatusDto {
    @NotNull
    private Status status;
}
