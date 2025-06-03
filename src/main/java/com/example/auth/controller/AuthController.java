package com.example.auth.controller;


import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;

public interface AuthController {

    ResponseEntity<UserRegisterResponse> registerUser(
            UserRegisterRequest registerRequest,
            HttpServletResponse response);

    ResponseEntity<Void> refreshTokens(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response);
}
