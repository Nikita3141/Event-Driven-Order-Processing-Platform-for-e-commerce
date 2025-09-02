package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.dto.JwtAuthenticationResponse;
import com.ecommerce.platform.authservice.dto.RegisterRequest;
import com.ecommerce.platform.authservice.dto.SignInRequest;
import com.ecommerce.platform.authservice.exception.InvalidTokenException;
import com.ecommerce.platform.authservice.exception.UserNotFoundException;
import com.ecommerce.platform.authservice.model.RefreshToken;
import com.ecommerce.platform.authservice.model.Role;
import com.ecommerce.platform.authservice.model.User;
import com.ecommerce.platform.authservice.repository.RefreshTokenRepository;
import com.ecommerce.platform.authservice.repository.RoleRepository;
import com.ecommerce.platform.authservice.security.JwtService;
import com.ecommerce.platform.authservice.security.RefreshTokenService;
import dto.AuthResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public JwtAuthenticationResponse authenticate(SignInRequest request) {
        log.info("Authentication attempt for email: {}", request.getEmail());

        User user = userService.getUserByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + request.getEmail()));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }

        String accessToken = jwtService.generateToken(user);

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new JwtAuthenticationResponse(
                accessToken,
                refreshToken.getRefreshToken()
        );
    }

    public void logout (String username){
        User user = userService.getUserByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + username));

        refreshTokenService.deletByUser(user);
    }

    @Override
    public JwtAuthenticationResponse register(RegisterRequest request) {

        log.info("Registration attempt for email: {}", request.getEmail());

        User user = userService.createUser(request);
        String jwtToken = jwtService.generateToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user.getId());

        log.info("User {} successfully registered", user.getEmail());

        return new JwtAuthenticationResponse(
                jwtToken,
                refreshToken.getRefreshToken()
        );
    }
}
