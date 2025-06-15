package com.example.auth.controller.auth;


import com.example.auth.dto.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthController {

    ResponseEntity<UserRegisterResponse> signUp(
            UserRegisterRequest registerRequest,
            HttpServletResponse response);

    ResponseEntity<Void> refreshTokens(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response);

    ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletResponse servletResponse);


}
