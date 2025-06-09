package com.example.auth.service.password;

import com.example.auth.dto.ForgotPasswordRequest;
import com.example.auth.entity.User;
import com.example.auth.event.PasswordResentEvent;
import com.example.auth.exception.user.UserNotFoundException;
import com.example.auth.service.redis.RedisPasswordResetTokenStorage;
import com.example.auth.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordResetServiceImpl implements PasswordResetService {

    private final RedisPasswordResetTokenStorage resetTokenStorage;
    private final UserService userService;
    private final ApplicationEventPublisher publisher;


    @Override
    public void requestPasswordReset(ForgotPasswordRequest request) {
        try {
            User user = userService.findByEmail(request.email());
            String token = generateToken();
            resetTokenStorage.saveToken(user.getEmail(), token);
            Instant expireTime = resetTokenStorage.getDefaultExpireTime();
            publisher.publishEvent(new PasswordResentEvent(user,
                    token,
                    expireTime,
                    generateResetLink(user.getEmail(), token)));
        } catch (UserNotFoundException e) {
            log.warn("Password reset requested for non-existing email: {}", e.getMessage());
        }
    }


    @Override
    public void resetPassword(String token, String newPassword) {

        String email = resetTokenStorage.getEmailByToken(token);
        if (!resetTokenStorage.isTokenValid(email, token)) {
            throw new IllegalStateException("ТОКЕН НЕ ВАЛДИНЫЙ");
        }
        userService.updatePassword(email,newPassword);
        resetTokenStorage.deleteToken(email);
    }

    @Override
    public void revokeToken(String email) {

    }

    @Override
    public boolean hasActiveResetRequest(String email) {
        return false;
    }

    @Override
    public String generateToken() {
        return PasswordResetService.super.generateToken();
    }

    @Override
    public String generateResetLink(String email, String token) {
        return PasswordResetService.super.generateResetLink(email, token);
    }
}
