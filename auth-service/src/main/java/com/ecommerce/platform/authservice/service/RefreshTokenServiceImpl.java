package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.exception.InvalidTokenException;
import com.ecommerce.platform.authservice.model.RefreshToken;
import com.ecommerce.platform.authservice.model.User;
import com.ecommerce.platform.authservice.repository.RefreshTokenRepository;
import com.ecommerce.platform.authservice.repository.UserRepository;
import com.ecommerce.platform.authservice.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenDurationMs;

    /**
     * Создает новый refresh-токен для пользователя
     */
    @Override
    public RefreshToken createRefreshToken(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshTokenDurationMs),
                user
        );

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        log.info("Refresh token created for user: {}", user.getId());
        return savedToken;
    }

    @Override
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("Token cannot be null or empty");
        }
        boolean isValid = jwtService.isTokenValid(token);

        if (isValid) {
            log.info("Token is valid: {}", token);
        } else {
            log.warn("Token is invalid: {}", token);
        }
        return isValid;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public void deleteAllByUser(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }

    @Override
    @Transactional
    public void verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token); // Опционально: удаляем просроченный токен
            throw new InvalidTokenException("Refresh token was expired");
        }
    }

    @Override
    @Transactional
    public void invalidate(RefreshToken token) {
        refreshTokenRepository.delete(token);
        log.debug("Refresh token deleted: {}", token.getToken());
    }

    @Override
    @Transactional
    public void deleteAllExpired() {
        refreshTokenRepository.deleteAllExpired();
    }


}
