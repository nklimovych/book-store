package mate.academy.bookstore.service.impl;

import java.util.Collections;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.user.UserRegistrationRequestDto;
import mate.academy.bookstore.dto.user.UserRegistrationResponseDto;
import mate.academy.bookstore.exception.RegistrationException;
import mate.academy.bookstore.mapper.UserMapper;
import mate.academy.bookstore.model.RoleName;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.role.RoleRepository;
import mate.academy.bookstore.repository.user.UserRepository;
import mate.academy.bookstore.service.ShoppingCartService;
import mate.academy.bookstore.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final ShoppingCartService shoppingCartService;

    @Override
    public UserRegistrationResponseDto save(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        String email = requestDto.getEmail();
        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new RegistrationException("User with email " + email + " already exists");
        }

        User user = userMapper.toModel(requestDto);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singleton(roleRepository.findByName(RoleName.USER)));
        User savedUser = userRepository.save(user);

        shoppingCartService.create(savedUser);
        return userMapper.toDto(savedUser);
    }
}
