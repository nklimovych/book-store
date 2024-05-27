package mate.academy.bookstore.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {
    private Long id;
    @NotBlank
    @Size(min = 4, max = 24, message = "length should be 4 to 24 characters long")
    private String name;
    private String description;
}
