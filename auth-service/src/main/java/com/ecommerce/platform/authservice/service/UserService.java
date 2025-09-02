package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.dto.RegisterRequest;
import com.ecommerce.platform.authservice.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;


public interface UserService extends UserDetailsService {
    User createUser(RegisterRequest request);
    Optional<User> getUserById(long userId); // Возвращает Optional
    Optional<User> getUserByEmail(String email); // Возвращает Optional
    boolean existsByEmail(String email);
    UserDetails loadUserByUsername(String username);
}
