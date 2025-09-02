package com.ecommerce.platform.authservice.controller;


import com.ecommerce.platform.authservice.dto.JwtAuthenticationResponse;
import com.ecommerce.platform.authservice.dto.RegisterRequest;
import com.ecommerce.platform.authservice.dto.SignInRequest;
import com.ecommerce.platform.authservice.model.RefreshToken;
import com.ecommerce.platform.authservice.model.User;
import com.ecommerce.platform.authservice.security.JwtService;
import com.ecommerce.platform.authservice.security.RefreshTokenService;
import com.ecommerce.platform.authservice.service.AuthService;
import dto.AuthResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@Tag(name = "Authentication", description = "API для аутентификации и управления токенами")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, RefreshTokenService refreshTokenService, JwtService jwtService) {
        this.authService = authService;
        this.refreshTokenService = refreshTokenService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticate(@Valid @RequestBody SignInRequest request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(Authentication authentication) {
        String username = authentication.getName(); // Spring Security даст текущего пользователя
        authService.logout(username);
        return ResponseEntity.ok("Successfully logged out");
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh (@RequestBody Map<String, String> request){
        String refreshTokenStr = request.get("refreshToken");

        RefreshToken refreshToken = refreshTokenService.findByToken(refreshTokenStr)
                .map(refreshTokenService::verifyExpiration)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        User user = refreshToken.getUser();
        String newAccessToken = jwtService.generateToken(user);

        return ResponseEntity.ok(new JwtAuthenticationResponse(newAccessToken, refreshTokenStr));
    }
}