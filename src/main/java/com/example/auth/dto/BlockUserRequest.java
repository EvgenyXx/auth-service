package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record BlockUserRequest(
        @NotBlank(message = "обязательное поле") UUID userId,
        boolean isBlocked
) {
}
