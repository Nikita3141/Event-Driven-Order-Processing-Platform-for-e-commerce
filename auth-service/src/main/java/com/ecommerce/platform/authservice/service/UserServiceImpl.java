package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.dto.RegisterRequest;
import com.ecommerce.platform.authservice.exception.UserAlreadyExistsException;
import com.ecommerce.platform.authservice.model.Role;
import com.ecommerce.platform.authservice.model.User;
import com.ecommerce.platform.authservice.repository.RoleRepository;
import com.ecommerce.platform.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public User createUser(RegisterRequest request) {
        if(existsByEmail(request.getEmail())){
            log.warn("Email {} already exists", request.getEmail());
            throw new UserAlreadyExistsException("Email already in use");
        }

        Role userRole = roleRepository.findByName("ROLE_USER");

        User user =  userRepository.save(
                User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .roles(Set.of(userRole))

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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
    }

}
