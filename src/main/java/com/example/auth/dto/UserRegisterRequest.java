package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "DTO for user registration request")
public class UserRegisterRequest {

    private static final String BAD_REQUEST_MESSAGE = "Поле обязательно для заполнения";

    @NotBlank(message = BAD_REQUEST_MESSAGE)
    @Pattern(regexp = "^(\\+7|8)[\\s\\-()]?\\d{3}[\\s\\-()]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$",
            message = "Номер должен начинаться с +7 или 8. Пример: '+79181234567'")
    @Schema(
            description = "User's phone number in Russian format",
            example = "+79181234567",
            requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^(\\+7|8)[\\s\\-()]?\\d{3}[\\s\\-()]?\\d{3}[\\s\\-]?\\d{2}[\\s\\-]?\\d{2}$"
    )
    private String numberPhone;

    @Email(message = "Электронная почта введена не верно! Попробуй еще раз!")
    @NotBlank(message = BAD_REQUEST_MESSAGE)
    @Schema(
            description = "User's email address",
            example = "user@example.com",
            requiredMode = Schema.RequiredMode.REQUIRED,
            format = "email"
    )
    private String email;

    @NotBlank(message = BAD_REQUEST_MESSAGE)
    @Size(min = 6, max = 20, message = "Длина пароля должна быть не меньше '6' символов и не больше '100'! ")
    @Schema(
            description = "User's password",
            example = "MySecurePassword123",
            requiredMode = Schema.RequiredMode.REQUIRED,
            minLength = 6,
            maxLength = 20
    )
    private String password;

    @NotBlank(message = BAD_REQUEST_MESSAGE)
    @Pattern(regexp = "^[а-яА-Я]+$", message = "Имя должно содержать только русские буквы")
    @Schema(
            description = "User's first name (only Russian letters allowed)",
            example = "Иван",
            requiredMode = Schema.RequiredMode.REQUIRED,
            pattern = "^[а-яА-Я]+$"
    )
    private String firstname;
}