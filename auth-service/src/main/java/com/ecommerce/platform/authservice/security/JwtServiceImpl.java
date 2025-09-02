package com.ecommerce.platform.authservice.security;
import com.ecommerce.platform.authservice.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;

/**
 * Сервис для работы с JWT токенами:
 * - Генерация access/refresh токенов
 * - Валидация токенов
 * - Извлечение данных из токенов
 */
@Service
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-expiration}") // в мс, например 900000 = 15 мин
    private long expiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getUsernameFromToken(String token) {
        return getClaims(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getClaims(token).get("roles", List.class);
    }

    public boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return !isTokenExpired(token);
        } catch (ExpiredJwtException e) {
            log.warn("Токен просрочен: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Неподдерживаемый токен: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Некорректный токен: {}", e.getMessage());
        } catch (SignatureException e) {
            log.error("Неверная подпись токена: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Пустой токен: {}", e.getMessage());
        }
        return false;
    }
}

