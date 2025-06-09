package com.example.auth.controller.password;

import com.example.auth.dto.ForgotPasswordRequest;
import com.example.auth.dto.PasswordResetRequest;
import org.springframework.http.ResponseEntity;

public interface PasswordResetController {


    ResponseEntity<Void> requestPasswordReset(ForgotPasswordRequest request);

    ResponseEntity<Void> resetPassword(PasswordResetRequest request);
}
