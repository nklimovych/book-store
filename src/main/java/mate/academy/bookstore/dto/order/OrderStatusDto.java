package mate.academy.bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderStatusDto {
    @NotBlank
    private String status;
}
