package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.dto.RefreshTokenRequest;
import com.ecommerce.platform.authservice.model.User;
import dto.AuthRequestDto;
import dto.AuthResponseDto;

public interface AuthService {

    /**
     * Аутентификация пользователя
     */
    AuthResponseDto authenticate(AuthRequestDto request);

    /**
     * Обновление пары токенов
     */
    AuthResponseDto refreshToken(RefreshTokenRequest request);

    /**
     * Выход из системы (инвалидация токенов)
     */
    void logout(String refreshToken);

    /**
     * Выход из всех устройств
     */
    void logoutAll(User user);
}
