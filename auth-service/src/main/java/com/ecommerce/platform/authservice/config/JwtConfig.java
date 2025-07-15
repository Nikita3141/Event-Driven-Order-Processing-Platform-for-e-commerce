package com.ecommerce.platform.authservice.config;


import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.util.Base64;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {

    private String secret ;
    private long accessExpiration;
    private long refreshExpiration;
    private String issuer;

    /**
     * Преобразует секретную строку в криптографический ключ
     * @return SecretKey для подписи/верификации
     */

    @Bean
    public SecretKey secretKey() {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        return Keys.hmacShaKeyFor(decodedKey);
    }

    @Bean
    public JwtParser jwtParser(SecretKey secretKey, JwtConfig jwtconfig) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .requireIssuer(this.issuer)
                .build();
    }

}
