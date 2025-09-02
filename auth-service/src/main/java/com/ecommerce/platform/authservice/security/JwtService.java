package com.ecommerce.platform.authservice.security;

import com.ecommerce.platform.authservice.model.User;
import io.jsonwebtoken.JwtException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtService {

    /**
     * Генерация refresh токена
     * @param user объект пользователя
     * @return JWT токен с длительным временем жизни
     */
    String generateToken(User user);

    /**
     * Проверяет валидность токена
     * @param token JWT токен
     * @return true если токен валиден, false если просрочен или подпись неверна
     */
    boolean validateToken(String token);

    /**
     * Извлекает имя пользователя (email) из токена
     * @param token JWT токен
     * @return email пользователя
     * @throws JwtException если токен невалиден
     */
    String getUsernameFromToken(String token);

    List<String> getRoles(String token);

    boolean isTokenExpired(String token);

}
