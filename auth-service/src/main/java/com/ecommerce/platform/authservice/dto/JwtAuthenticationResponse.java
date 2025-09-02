package com.ecommerce.platform.authservice.dto;


import com.ecommerce.platform.authservice.model.RefreshToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Ответ c токенами доступа")
public class JwtAuthenticationResponse {

    @Schema(description = "AccessToken доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String accessToken;

    @Schema(description = "RefreshToken доступа", example = "1570bd7e-8baa-4ccb-857e-8b8f3b4a...")
    private String refreshToken;

}
