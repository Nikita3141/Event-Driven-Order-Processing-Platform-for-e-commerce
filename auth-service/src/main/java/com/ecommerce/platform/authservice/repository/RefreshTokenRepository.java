package com.ecommerce.platform.authservice.repository;


import com.ecommerce.platform.authservice.model.RefreshToken;
import com.ecommerce.platform.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByRefreshToken(String token);
    void deleteByUser(User user);
}
