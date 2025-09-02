package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.dto.RefreshTokenRequest;
import com.ecommerce.platform.authservice.exception.InvalidTokenException;
import com.ecommerce.platform.authservice.exception.UserNotFoundException;
import dto.AuthRequestDto;
import dto.AuthResponseDto;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * Сервис аутентификации и управления токенами.
 * Обрабатывает вход, обновление токенов и выход из системы.
 */
public interface AuthService {

    /**
     * Аутентификация пользователя по email и паролю.
     *
     * @param request DTO с email и паролем
     * @return AuthResponseDto с access/refresh токенами
     * @throws BadCredentialsException если неверные учетные данные
     * @throws UserNotFoundException если пользователь не существует
     */
    AuthResponseDto authenticate(AuthRequestDto request);

    /**
     * Обновляет пару токенов по валидному refresh-токену.
     *
     * @param request DTO с refresh-токеном
     * @return AuthResponseDto с новой парой токенов
     * @throws InvalidTokenException если токен невалиден
     * @throws ExpiredTokenException если токен истёк
     */
    AuthResponseDto refreshToken(RefreshTokenRequest request);

    /**
     * Инвалидирует refresh-токен и связанный access-токен.
     *
     * @param refreshToken Токен для инвалидации
     */
    void logout(String refreshToken);

    /**
     * Инвалидирует ВСЕ активные сеансы пользователя.
     *
     * @param userId ID пользователя
     * @throws UserNotFoundException если пользователь не существует
     */
    void logoutAll(Long userId);

    AuthResponseDto register(AuthRequestDto request);
}
