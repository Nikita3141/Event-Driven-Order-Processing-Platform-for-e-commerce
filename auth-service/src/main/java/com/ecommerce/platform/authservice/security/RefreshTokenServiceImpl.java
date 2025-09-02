package com.ecommerce.platform.authservice.security;


import com.ecommerce.platform.authservice.exception.UserNotFoundException;
import com.ecommerce.platform.authservice.model.RefreshToken;
import com.ecommerce.platform.authservice.model.User;
import com.ecommerce.platform.authservice.repository.RefreshTokenRepository;
import com.ecommerce.platform.authservice.repository.UserRepository;
import com.ecommerce.platform.authservice.service.UserService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenDurationMs;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserService userService;

    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userService.getUserById(userId).orElseThrow(
                () -> new UserNotFoundException("User not found: id="+userId)
        ));
        refreshToken.setRefreshToken(UUID.randomUUID().toString());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));

        return refreshTokenRepository.save(refreshToken);

    }

    public RefreshToken verifyExpiration(RefreshToken refreshToken) {
        if(refreshToken.getExpiryDate().isBefore(Instant.now())){
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException ("Токен просрочен");
        }

        return refreshToken;
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByRefreshToken(token);
    }

    public void deletByUser(User user){
        refreshTokenRepository.deleteByUser(user);
    }
}
