package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.exception.UserAlreadyExistsException;
import com.ecommerce.platform.authservice.exception.UserNotFoundException;
import com.ecommerce.platform.authservice.model.User;
import com.ecommerce.platform.authservice.repository.UserRepository;
import dto.AuthRequestDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(AuthRequestDto request) {
        if(existsByEmail(request.email())){
            log.warn("Email {} already exists", request.email());
            throw new UserAlreadyExistsException("Email already in use");
        }
        User user =  userRepository.save(
                User.builder()
                    .email(request.email())
                    .password(passwordEncoder.encode(request.password()))
                    .build()
        );
        log.info("User created with ID: {}", user.getId());
        return user;

    }

    @Override
    public Optional<User>  getUserById(long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
