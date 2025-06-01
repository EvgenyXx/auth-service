package com.example.auth.controller;

import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface AuthController {

    ResponseEntity<UserRegisterResponse> registerUser(
            UserRegisterRequest registerRequest,
            HttpServletResponse response);
}
