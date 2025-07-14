package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.model.RefreshToken;
import com.ecommerce.platform.authservice.model.User;
import com.ecommerce.platform.authservice.repository.RefreshTokenRepository;
import com.ecommerce.platform.authservice.repository.UserRepository;
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
    private final UserRepository userRepository;

    @Value("${jwt.refresh-expiration}")
    private long refreshTokenDurationMs;

    /**
     * Создает новый refresh-токен для пользователя
     */
    @Override
    public RefreshToken createRefreshToken(User user) {
        if (user == null) {
            log.warn("User cannot be null");
        }
        RefreshToken refreshToken = new RefreshToken(
                UUID.randomUUID().toString(),
                Instant.now().plusMillis(refreshTokenDurationMs),
                user
        );
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public boolean validateToken(String token) {
        if (token == null || token.isBlank()) {
            log.warn("Token cannot be null or empty");
        }
        return refreshTokenRepository.findByToken(token)
                .map(t -> !t.isExpired())
                .orElse(false);
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
    public void deleteAllExpired() {
        refreshTokenRepository.deleteAllExpired();
    }


}
