package com.example.auth.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@Schema(description = "DTO containing user registration response data")
public class UserRegisterResponse {

    @Schema(
            description = "Unique identifier of the user",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private UUID id;

    @Schema(
            description = "User's phone number",
            example = "+79181234567",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String numberPhone;

    @Schema(
            description = "User's email address",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String email;

    @Schema(
            description = "User's first name",
            example = "Иван",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String firstname;

    @Schema(
            description = "Timestamp when user was created",
            example = "2023-12-31T23:59:59",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private LocalDateTime createdAt;

    @JsonIgnore
    @Schema(hidden = true)  // This will completely hide the field from Swagger documentation
    private String accessToken;

    @JsonIgnore
    @Schema(hidden = true)  // This will completely hide the field from Swagger documentation
    private String refreshToken;

    @Schema(hidden = true)  // Hide the method from Swagger documentation
    public UserRegisterResponse withoutTokens() {
        return UserRegisterResponse.builder()
                .id(this.id)
                .email(this.email)
                .createdAt(this.createdAt)
                .firstname(this.firstname)
                .numberPhone(this.numberPhone)
                .build();
    }
}