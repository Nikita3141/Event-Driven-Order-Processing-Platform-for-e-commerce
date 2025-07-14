package com.ecommerce.platform.authservice.repository;


import com.ecommerce.platform.authservice.model.RefreshToken;
import com.ecommerce.platform.authservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    /**
     * Находит токен по его значению
     */
    Optional<RefreshToken> findByToken(String token);
    /**
     * Находит токен, связанный с пользователем
     */
    Optional<RefreshToken> findByUser( User user);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user = :user ")
    void deleteAllByUser(@Param("user") User user);

    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE  rt.expiryDate < CURRENT_TIMESTAMP ")
    void deleteAllExpired();
}
