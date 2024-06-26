package mate.academy.bookstore.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequestDto(
        @NotBlank
        @Size(min = 8, max = 20)
        @Email
        String email,
        @NotBlank
        @Size(min = 8, max = 20, message = "Password must be at least 8 characters long")
        String password
) {
}
