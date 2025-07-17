package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.exception.UserAlreadyExistsException;
import com.ecommerce.platform.authservice.model.Role;
import com.ecommerce.platform.authservice.model.User;
import com.ecommerce.platform.authservice.repository.UserRepository;
import dto.AuthRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public org.springframework.security.core.userdetails.User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found : "+username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(),
                mapRolesToAuthorities(user.getRoles()));
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles){
        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole())).collect(Collectors.toList());
    }
}
