package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.dto.RefreshTokenRequest;
import com.ecommerce.platform.authservice.exception.InvalidTokenException;
import com.ecommerce.platform.authservice.exception.UserNotFoundException;
import com.ecommerce.platform.authservice.model.RefreshToken;
import com.ecommerce.platform.authservice.model.User;
import com.ecommerce.platform.authservice.security.JwtService;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public AuthResponseDto authenticate(AuthRequestDto request) {
        log.info("Authentication attempt for email: {}", request.email());

        User user = userService.getUserByEmail(request.email()).get();

        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid password");
        }
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = refreshTokenService.createRefreshToken(user).getToken();

        return new AuthResponseDto(
                accessToken,
                refreshToken,
                jwtService.getAccessTokenExpirationInMillis()
        );
    }

    @Override
    public AuthResponseDto refreshToken(RefreshTokenRequest request) {
        log.debug("Refreshing token with refresh token: {}", request.refreshToken());

        RefreshToken refreshToken = refreshTokenService.findByToken(request.refreshToken())
                .orElseThrow(() -> new InvalidTokenException("Invalid refresh token"));

        refreshTokenService.verifyExpiration(refreshToken);

        User user = userService.getUserById(refreshToken.getUser().getId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        String newAccessToken = jwtService.generateAccessToken(user);
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);

        log.info("Tokens refreshed for user: {}", user.getEmail());

        return new AuthResponseDto(
                newAccessToken,
                newRefreshToken.getToken(),
                jwtService.getAccessTokenExpirationInMillis()
        );
    }

    @Override
    public void logout(String refreshToken) {
        log.debug("Logout attempt with refresh token");
        refreshTokenService.findByToken(refreshToken)
                .ifPresentOrElse(
                        token -> {
                            refreshTokenService.invalidate(token);
                            log.info("User {} logged out", token.getUser().getEmail());
                        },
                        () -> log.warn("Invalid refresh token provided for logout")
                );
    }

    @Override
    public void logoutAll(Long userId) {
        log.info("Logging out all sessions for user ID: {}", userId);
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        refreshTokenService.deleteAllByUser(user);
    }
}
