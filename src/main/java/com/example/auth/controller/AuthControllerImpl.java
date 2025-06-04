package com.example.auth.controller;


import com.example.auth.dto.*;
import com.example.auth.service.jwt.AuthTokenService;
import com.example.auth.service.jwt.CookieService;
import com.example.auth.service.register.UserRegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerImpl implements AuthController {

    private static final String AUTH_HEADER_PREFIX = "Bearer ";

    private final UserRegistrationService userRegistrationService;
    private final CookieService cookieService;
    private final AuthTokenService authTokenService;

    @Override
    @PostMapping("/register")
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

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest,HttpServletResponse servletResponse) {
        LoginResponse loginResponse = userRegistrationService.login(loginRequest);
        cookieService.setRefreshTokenCookie(servletResponse, loginResponse.getRefreshToken());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION,AUTH_HEADER_PREFIX + loginResponse.getAccessToken())
                .body(loginResponse);
    }


}
