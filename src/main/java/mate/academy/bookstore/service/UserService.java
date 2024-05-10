package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.user.UserResponseDto;
import mate.academy.bookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto save(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
