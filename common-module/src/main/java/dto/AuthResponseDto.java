package dto;

public record AuthResponseDto(
        String accessToken,
        String refreshToken,
        long expiresIn
) {}
