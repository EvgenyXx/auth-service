package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Schema(description = "DTO for user login request")
public class LoginRequest {
    private static final String BAD_REQUEST_MESSAGE = "Поле обязательно для заполнения";

    @NotBlank(message = BAD_REQUEST_MESSAGE)
    @Pattern(regexp = "^(\\+7|8)[\\s\\-()]?\\d{3}[\\s\\-()]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$",
            message = "Номер должен начинаться с +7 или 8. Пример: '+79181234567'")
    @Schema(
            description = "User's phone number in Russian format",
            example = "+79181234567",
            requiredMode = Schema.RequiredMode.REQUIRED
    )
    private String numberPhone;

    @NotBlank(message = BAD_REQUEST_MESSAGE)
    @Size(min = 6, max = 20, message =
            "Длина пароля должна быть не меньше '6' символов и не больше '100'! ")
    @Schema(
            description = "User's password",
            example = "mySecurePassword123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6,
            maxLength = 20
    )
    private String rawPassword;
}