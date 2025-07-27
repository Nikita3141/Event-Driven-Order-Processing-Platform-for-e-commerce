package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.model.User;
import dto.AuthRequestDto;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface UserService extends UserDetailsService {
    User createUser(AuthRequestDto request);
    Optional<User> getUserById(long userId); // Возвращает Optional
    Optional<User> getUserByEmail(String email); // Возвращает Optional
    boolean existsByEmail(String email);
    org.springframework.security.core.userdetails.User loadUserByUsername(String username);
}
