package com.ecommerce.platform.authservice.security;


import constant.Constant;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NotNull HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain filterChain
    ) throws ServletException, IOException {
        // 1. Получаем заголовок Authorization
        final String authHeader = request.getHeader(Constant.AUTH_HEADER);

        // 2. Проверяем наличие и формат заголовка
        if (authHeader == null || !authHeader.startsWith(Constant.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Извлекаем токен (после "Bearer ")
        final String jwt = authHeader.substring(Constant.BEARER_PREFIX.length());
        // 4. Проверяем токен
        if(jwtService.validateToken(jwt)){
            // 5. Получаем имя пользователя
            String username = jwtService.getUsernameFromToken(jwt);
            // 6. Получаем пользователя из БД
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            // 7. Создаём объект аутентификации

            List<String> roles = jwtService.getRoles(jwt);

            var authorities = roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            authorities);
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request));
            // 8. Сохраняем в SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        }
        // 9. Передаём запрос дальше по цепочке фильтров
        filterChain.doFilter(request, response);
    }
}
