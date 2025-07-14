package com.ecommerce.platform.authservice.service;

import com.ecommerce.platform.authservice.dto.RefreshTokenRequest;
import com.ecommerce.platform.authservice.model.User;
import dto.AuthRequestDto;
import dto.AuthResponseDto;

public class AuthServiceImpl implements AuthService {

    @Override
    public AuthResponseDto authenticate(AuthRequestDto request) {
        return null;
    }

    @Override
    public AuthResponseDto refreshToken(RefreshTokenRequest request) {
        return null;
    }

    @Override
    public void logout(String refreshToken) {

    }

    @Override
    public void logoutAll(User user) {

    }
}
