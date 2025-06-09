package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(
        name = "PasswordResetRequest",
        description = "Запрос на сброс пароля пользователя"
)
public record PasswordResetRequest(
        @Schema(
                description = "Уникальный токен для сброса пароля (формат UUID)",
                example = "a1b2c3d4-e5f6-7890-g1h2-3456ij7890kl"
        )
        @NotBlank(message = "Токен обязателен")
        @Pattern(
                regexp = "^[\\da-f]{8}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{4}-[\\da-f]{12}$",
                message = "Неверный формат токена. Ожидается UUID (пример: a1b2c3d4-e5f6-7890-g1h2-3456ij7890kl)"
        )
        String token,

        @Schema(
                description = "Новый пароль пользователя",
                example = "SecurePass123!"
        )
        @NotBlank(message = "Пароль обязателен")
        @Size(min = 8, max = 64, message = "Пароль должен быть от 8 до 64 символов")
        @Pattern(
                regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[\\W_]).*$",
                message = "Пароль должен содержать цифры, буквы в верхнем и нижнем регистре и спецсимволы"
        )
        String newPassword
) {}