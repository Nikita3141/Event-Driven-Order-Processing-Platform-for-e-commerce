package com.ecommerce.platform.authservice.security;
import com.ecommerce.platform.authservice.config.JwtConfig;
import com.ecommerce.platform.authservice.model.User;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

/**
 * Сервис для работы с JWT токенами:
 * - Генерация access/refresh токенов
 * - Валидация токенов
 * - Извлечение данных из токенов
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class JwtServiceImpl {
    // Ключ для подписи токенов (HMAC-SHA256)
    private final SecretKey signKey;
    // Время жизни access токена в миллисекундах
    private final JwtConfig jwtConfig;
    // Время жизни refresh токена в миллисекундах
    private final JwtParser jwtParser;

    /**
     * Генерация access токена
     * @param user объект пользователя
     * @return JWT токен с коротким временем жизни
     */
    public String generateAccessToken(User user) {
        String token = buildToken(user, jwtConfig.getAccessExpiration());
        log.debug("Generated access token for user: {}", user.getEmail());
        return token;
    }

    /**
     * Генерация refresh токена
     * @param user объект пользователя
     * @return JWT токен с длительным временем жизни
     */
    public String generateRefreshToken(User user) {
        String token = buildToken(user, jwtConfig.getRefreshExpiration());
        log.debug("Generated refresh token for user: {}", user.getEmail());
        return token;
    }

    /**
     * Внутренний метод построения токена
     * @param user объект пользователя
     * @param expiration время жизни токена в мс
     * @return подписанный JWT токен
     */
    private String buildToken(User user, long expiration) {
        return Jwts.builder()
                .issuer(jwtConfig.getIssuer())
                .subject(user.getEmail())          // Устанавливаем subject (email пользователя)
                .issuedAt(Date.from(Instant.now())) // Время создания токена
                .expiration(Date.from(Instant.now().plusMillis(expiration))) // Время истечения
                .signWith(signKey)                 // Подписываем токен
                .compact();                        // Преобразуем в строку
    }

    /**
     * Проверяет валидность токена
     * @param token JWT токен
     * @return true если токен валиден, false если просрочен или подпись неверна
     */
    public boolean isTokenValid(String token) {
        if(token == null || token.isBlank()){
            log.warn("Token is null or blank");
            return false;
        }

        try {

            Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);

            if(claimsJws.getPayload().getExpiration().before(new Date())){
                log.warn("Token is expired");
                return false;
            }

            if(!claimsJws.getPayload().getIssuer().equals(jwtConfig.getIssuer())){
                log.warn("Invalid token issuer");
                return false;
            }

            log.debug("Token validation successful");
            claimsJws.getPayload().getSubject();
            return true;

        } catch (ExpiredJwtException ex) {
            log.warn("Token expired: {}", ex.getMessage());
            return false;
        } catch (MalformedJwtException ex) {
            log.warn("Invalid token format: {}", ex.getMessage());
            return false;
        } catch (SecurityException ex) {
            log.warn("Invalid signature: {}", ex.getMessage());
            return false;
        } catch (IllegalArgumentException ex) {
            log.warn("Empty or null token");
            return false;
        }
    }

    /**
     * Извлекает имя пользователя (email) из токена
     * @param token JWT токен
     * @return email пользователя
     * @throws JwtException если токен невалиден
     */
    public String extractUsername(String token) {
        try {
            String username = jwtParser
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
            log.trace("Extracted username from token: {}", username);
            return username;
        } catch (JwtException ex) {
            log.error("Failed to extract username from token: {}", ex.getMessage());
            throw ex;
        }
    }

    public boolean isTokenValidForUser(String token , UserDetails userDetails) {
        if(!isTokenValid(token)){
            return false;
        }

        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername());

    }
}
