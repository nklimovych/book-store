package mate.academy.bookstore.service.impl;

import mate.academy.bookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.user.UserResponseDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.mapper.UserMapper;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.user.UserRepository;
import mate.academy.bookstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserResponseDto save(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        String email = requestDto.getEmail().toLowerCase();
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RegistrationException("User with email " + email + " already exists");
        }

        User user = userMapper.toModel(requestDto);
        user.setEmail(email);
        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
