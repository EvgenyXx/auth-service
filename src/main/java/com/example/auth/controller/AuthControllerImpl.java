package com.example.auth.controller;

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

import java.time.Duration;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/register")
public class AuthControllerImpl implements AuthController {

    private final UserRegistrationService userRegistrationService;

    @Override
    @PostMapping
    public ResponseEntity<UserRegisterResponse> registerUser(
            @Valid @RequestBody UserRegisterRequest registerRequest,
            HttpServletResponse response) {
        UserRegisterResponse registerResponse = userRegistrationService.registerUser(registerRequest);

        ResponseCookie responseCookie = ResponseCookie.from("refresh_token",registerResponse.getRefreshToken())
                .httpOnly(true)
                .maxAge(Duration.ofDays(30))
                .path("/api/v1/register")
                .secure(true)
                .sameSite("Strict")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE,responseCookie.toString());

        UserRegisterResponse userRegisterResponse = registerResponse.withoutTokens();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.AUTHORIZATION,"Bearer " + registerResponse.getAccessToken())
                .body(userRegisterResponse);

    }
}
