package com.example.auth.controller;

import com.example.auth.config.JwtCookieProperties;
import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;
import com.example.auth.service.register.UserRegistrationService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthControllerImpl implements AuthController {

    private final UserRegistrationService userRegistrationService;

    @Override
    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> registerUser(
            @Valid @RequestBody UserRegisterRequest registerRequest,
            HttpServletResponse response,
            JwtCookieProperties cookieProperties) {
        UserRegisterResponse registerResponse = userRegistrationService.registerUser(registerRequest);
        setRefreshTokenCookie(response, registerResponse,cookieProperties);
        UserRegisterResponse userRegisterResponse = registerResponse.withoutTokens();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + registerResponse.getAccessToken())
                .body(userRegisterResponse);

    }

    private void setRefreshTokenCookie(HttpServletResponse response,
                                       UserRegisterResponse registerResponse,
                                       JwtCookieProperties cookieProperties) {
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
}
