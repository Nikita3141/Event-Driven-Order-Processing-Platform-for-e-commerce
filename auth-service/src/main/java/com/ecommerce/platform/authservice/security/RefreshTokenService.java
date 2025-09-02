package com.ecommerce.platform.authservice.security;


import com.ecommerce.platform.authservice.model.RefreshToken;
import com.ecommerce.platform.authservice.model.User;

import java.util.Optional;

public interface RefreshTokenService {

     RefreshToken createRefreshToken(Long userId);

     Optional<RefreshToken> findByToken(String refreshToken);

     void deletByUser(User user);

     RefreshToken verifyExpiration(RefreshToken refreshToken);

}
