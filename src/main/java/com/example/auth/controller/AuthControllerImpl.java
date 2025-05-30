package com.example.auth.controller;

import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;
import com.example.auth.service.register.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/register")
public class AuthControllerImpl implements AuthController {

    private final UserRegistrationService userRegistrationService;

    @Override
    @PostMapping
    public ResponseEntity<UserRegisterResponse> registerUser(
            @Valid @RequestBody UserRegisterRequest registerRequest) {
        return new ResponseEntity<>(userRegistrationService.registerUser(registerRequest),HttpStatus.CREATED);
    }
}
