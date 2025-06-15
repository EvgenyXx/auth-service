package com.example.auth.controller.password;

import com.example.auth.dto.ForgotPasswordRequest;
import com.example.auth.dto.PasswordResetRequest;
import com.example.auth.service.password.PasswordResetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "Password Management",
        description = "Восстановление и сброс пароля")
public class PasswordResetControllerImpl implements PasswordResetController {



    private final PasswordResetService passwordResetService;

    @Operation(summary = "Отправка ссылки для сброса пароля",
            description = "Метод всегда возвращает 200 OK, даже если email не существует или произошла ошибка. " +
                    "Это сделано в целях безопасности, чтобы не раскрывать наличие email в системе.",
            responses = {
                    @ApiResponse(responseCode = "200",description = "Запрос принят в обработку")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Параметры для сброса пароля",
                    required = true
            ))
    @PostMapping("/password/forgot")
    @Override
    public ResponseEntity<Void> requestPasswordReset(@RequestBody @Valid ForgotPasswordRequest request) {
        passwordResetService.requestPasswordReset(request);
        return ResponseEntity.ok().build();
    }



    @Operation(
            summary = "Смена пароля через токен",
            description = "Завершающий этап сброса пароля. Принимает токен и новый пароль.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Пароль изменён"),
                    @ApiResponse(responseCode = "400", description = "Невалидные данные"),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Проблемы с токеном: просрочен или недействителен"
                    ),
                    @ApiResponse(
                            responseCode = "409",
                            description = "Активный запрос существует"
                    )
            }
    )
    @Override
    @PostMapping("/password/reset")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
