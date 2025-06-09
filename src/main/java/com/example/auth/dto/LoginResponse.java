package com.example.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LoginResponse {

    @Schema(description = "Номер телефона пользователя", example = "+79181234567")
    private String numberPhone;

    @Schema(description = "Email пользователя", example = "user@example.com")
    private String email;

    @Schema(description = "Имя пользователя", example = "John")
    private String firstname;

    @Schema(description = "Дата и время создания аккаунта пользователя", example = "2025-06-09T12:00:00")
    private LocalDateTime createdAt;

    @JsonIgnore
    @Schema(description = "Access токен пользователя. Не возвращается в ответах на клиентскую сторону.")
    private String accessToken;

    @JsonIgnore
    @Schema(description = "Refresh токен пользователя. Не возвращается в ответах на клиентскую сторону.")
    private String refreshToken;
}
