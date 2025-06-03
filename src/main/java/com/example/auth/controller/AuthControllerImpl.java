package com.example.auth.controller;

import com.example.auth.config.JwtCookieProperties;
import com.example.auth.dto.AuthTokens;
import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;
import com.example.auth.service.jwt.AuthTokenService;
import com.example.auth.service.register.UserRegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerImpl implements AuthController {

    private final UserRegistrationService userRegistrationService;
    private final JwtCookieProperties cookieProperties;
    private final AuthTokenService authTokenService;

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(
            @Valid @RequestBody UserRegisterRequest registerRequest,
            HttpServletResponse response) {
        UserRegisterResponse registerResponse = userRegistrationService.registerUser(registerRequest);
        setRefreshTokenCookie(response, registerResponse);
        UserRegisterResponse userRegisterResponse = registerResponse.withoutTokens();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + registerResponse.getAccessToken())
                .body(userRegisterResponse);

    }

    private void setRefreshTokenCookie(HttpServletResponse response,
                                       UserRegisterResponse registerResponse) {
        ResponseCookie responseCookie = ResponseCookie.from(cookieProperties.getName(),
                        registerResponse.getRefreshToken())
                .httpOnly(cookieProperties.isHttpOnly())
                .maxAge(cookieProperties.getMaxAge())
                .path(cookieProperties.getPath())
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshTokens(
            @CookieValue("${spring.security.jwt.cookie.name}") String refreshToken,
            HttpServletResponse response) {
        AuthTokens authTokens = authTokenService.refreshToken(refreshToken);
        ResponseCookie cookie = ResponseCookie.from(cookieProperties.getName(), authTokens.getRefreshToken())
                .secure(cookieProperties.isSecure())
                .sameSite(cookieProperties.getSameSite())
                .path(cookieProperties.getPath())
                .maxAge(cookieProperties.getMaxAge())
                .httpOnly(cookieProperties.isHttpOnly())
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authTokens.getAccessToken())
                .build();
    }


}
