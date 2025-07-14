package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.model.RefreshToken;
import com.ecommerce.platform.authservice.model.User;

import java.util.Optional;

/**
 * Сервис для работы с refresh-токенами.
 * Обеспечивает создание и валидацию токенов обновления.
 */
public interface RefreshTokenService {

    /**
     * Создает новый refresh-токен для пользователя.
     *
     * @param user пользователь, для которого создается токен
     * @return созданный refresh-токен
     * @throws IllegalArgumentException если пользователь равен null
     */
    RefreshToken createRefreshToken(User user);

    /**
     * Проверяет валидность refresh-токена.
     *
     * @param token токен для проверки
     * @return true если токен существует и не истек
     * @throws IllegalArgumentException если токен равен null или пуст
     */
    boolean validateToken(String token);

    /**
     * Находит refresh-токен по его значению.
     *
     * @param token значение токена
     * @return Optional с refresh-токеном или empty если не найден
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * Удаляет все refresh-токены пользователя.
     *
     * @param user пользователь, чьи токены нужно удалить
     */
    void deleteAllByUser(User user);

    void deleteAllExpired();
}
