package com.ecommerce.platform.authservice.security;

import com.ecommerce.platform.authservice.model.User;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    /**
     * Генерация access токена
     * @param user объект пользователя
     * @return JWT токен с коротким временем жизни
     */
    String generateAccessToken(User user);

    /**
     * Генерация refresh токена
     * @param user объект пользователя
     * @return JWT токен с длительным временем жизни
     */
    String generateRefreshToken(User user);

    /**
     * Проверяет валидность токена
     * @param token JWT токен
     * @return true если токен валиден, false если просрочен или подпись неверна
     */
    boolean isTokenValid(String token);

    /**
     * Извлекает имя пользователя (email) из токена
     * @param token JWT токен
     * @return email пользователя
     * @throws JwtException если токен невалиден
     */
    String extractUsername(String token) throws JwtException;

    /**
     * Проверяет валидность токена для пользователя
     * @param token JWT токен
     * @param userDetails объект UserDetails
     * @return true если токен валиден для пользователя, false иначе
     */
    boolean isTokenValidForUser(String token, UserDetails userDetails);
}
