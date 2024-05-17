package mate.academy.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryDto(
        Long id,
        @NotBlank
        @Size(min = 4, max = 24, message = "Category name must be 4 to 24 characters long")
        String name,
        String description
) {
}
