package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(
        description = "Запрос на восстановление пароля",
        example = "{\"email\": \"user@example.com\"}"
)
public record ForgotPasswordRequest(
        @Email(message =
                "Электронная почта введена не верно! Попробуй еще раз!")

        @NotBlank(message = "Почта введена не верно. Попробуйте еще раз")
        @Schema(description = "Почта для ввода",example = "evgenypavlov666@yandex.ru") String email

) {
}
