package mate.academy.bookstore.dto.cart;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuantityRequestDto {
    @NotNull
    @Min(1)
    private int quantity;
}
