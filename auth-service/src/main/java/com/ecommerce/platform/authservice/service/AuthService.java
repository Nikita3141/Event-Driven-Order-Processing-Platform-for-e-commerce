package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.dto.JwtAuthenticationResponse;
import com.ecommerce.platform.authservice.dto.RegisterRequest;
import com.ecommerce.platform.authservice.dto.SignInRequest;
import com.ecommerce.platform.authservice.exception.InvalidTokenException;
import com.ecommerce.platform.authservice.exception.UserNotFoundException;
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
    JwtAuthenticationResponse authenticate(SignInRequest request);

    JwtAuthenticationResponse register(RegisterRequest request);

    public void logout(String username);
}
