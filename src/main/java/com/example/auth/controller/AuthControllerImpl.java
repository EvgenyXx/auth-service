package com.example.auth.controller;


import com.example.auth.dto.*;
import com.example.auth.service.jwt.AuthTokenService;
import com.example.auth.service.jwt.CookieService;
import com.example.auth.service.password.PasswordResetService;
import com.example.auth.service.register.UserRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerImpl implements AuthController {

    private static final String AUTH_HEADER_PREFIX = "Bearer ";
    private static final String USER_TAG = "User";

    private final UserRegistrationService userRegistrationService;
    private final CookieService cookieService;
    private final AuthTokenService authTokenService;
    private final PasswordResetService passwordResetService;

    @Override
    @PostMapping("/register")
    @Operation(
            summary = "Метод для регистрации пользователей ",
            description = "Создает нового пользователя с уникальным номером телефона и email",
            responses = {
                    @ApiResponse(responseCode = "201", description = "При успешной регистрации пользователя"),
                    @ApiResponse(responseCode = "400", description = "Неверные данные"),
                    @ApiResponse(responseCode = "409", description = "Конфликт введённых данных с уже существующими в базе данных")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для регистрации нового пользователя", required = true
            ),
            tags = {USER_TAG, "Registration"})
    public ResponseEntity<UserRegisterResponse> registerUser(
            @Valid @RequestBody UserRegisterRequest registerRequest,
            HttpServletResponse response) {
        UserRegisterResponse registerResponse = userRegistrationService.registerUser(registerRequest);
        cookieService.setRefreshTokenCookie(response, registerResponse.getRefreshToken());
        UserRegisterResponse userRegisterResponse = registerResponse.withoutTokens();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER_PREFIX
                        + registerResponse.getAccessToken())
                .body(userRegisterResponse);

    }


    @Override
    @PostMapping("/refresh")
    @Operation(
            summary = "Обновляет пару токенов",
            description = "Генерирует новую пару access/refresh токенов на основе валидного refresh-" +
                    "токена из cookie",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Токены успешно обновлены. Новый access-токен возвращается в " +
                                    "заголовке Authorization, " +
                                    "refresh-токен — в cookie."),
                    @ApiResponse(responseCode = "401", description =
                            "Невалидный refresh-токен (отозван, просрочен или некорректен)"),

            },
            parameters = {
                    @Parameter(
                            name = "refreshToken",
                            description = "Refresh token из cookie для обновления токенов",
                            in = ParameterIn.COOKIE,
                            required = true
                    )
            }
    )
    public ResponseEntity<Void> refreshTokens(
            @CookieValue("${spring.security.jwt.cookie.name}") String refreshToken,
            HttpServletResponse response) {
        AuthTokens authTokens = authTokenService.refreshToken(refreshToken);
        cookieService.setRefreshTokenCookie(response, authTokens.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER_PREFIX
                        + authTokens.getAccessToken())
                .build();
    }

    @Override
    @PostMapping("/login")
    @Operation(
            summary = "Авторизация пользователя",
            description = "Метод для аутентификации пользователя. Принимает данные для входа (email и пароль), проверяет их " +
                    "на валидность и, если аутентификация успешна, генерирует пару токенов (access и refresh), возвращая их " +
                    "в ответ. Refresh токен сохраняется в cookie, а access токен передается в заголовке Authorization.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "При успешной авторизации, возвращены токены (access и refresh)."),
                    @ApiResponse(responseCode = "400", description = "Неверные данные (неправильный формат email или пароля)."),
                    @ApiResponse(responseCode = "401", description = "Неверные учетные данные (неверный пароль)."),
                    @ApiResponse(responseCode = "404", description = "Пользователь не найден по номеру телефона."),
                    @ApiResponse(responseCode = "423", description = "Аккаунт заблокирован. Попробуйте через некоторое время.")
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Данные для авторизации пользователя", required = true),
            tags = {USER_TAG, "Registration"}
    )
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest, HttpServletResponse servletResponse) {
        LoginResponse loginResponse = userRegistrationService.login(loginRequest);
        cookieService.setRefreshTokenCookie(servletResponse, loginResponse.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, AUTH_HEADER_PREFIX + loginResponse.getAccessToken())
                .body(loginResponse);
    }


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
    @PostMapping("/forgot-password")
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
    @PostMapping("/resent-password")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid PasswordResetRequest request) {
        passwordResetService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
