package com.example.auth.service.password;

import com.example.auth.dto.ForgotPasswordRequest;
import com.example.auth.entity.User;
import com.example.auth.event.PasswordResentEvent;
import com.example.auth.exception.token.ActiveResetRequestException;
import com.example.auth.exception.token.InvalidTokenException;
import com.example.auth.exception.token.ExpiredTokenException;
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
            if (hasActiveResetRequest(request.email())){
                throw new ActiveResetRequestException(
                        "На ваш email уже отправлена ссылка для смены пароля. " +
                                "Проверьте почту или повторите запрос позже."
                );
            }
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

        //TODO сделать исключение
        if (!resetTokenStorage.isTokenValid(token)) {
            // Проверяем отдельно, истёк ли токен
            if (!resetTokenStorage.isTokenActive(email)) {
                throw new ExpiredTokenException("Срок действия токена истёк. Запросите новый");
            }
            throw new InvalidTokenException("Токен недействителен");
        }
        userService.updatePassword(email,newPassword);
        resetTokenStorage.deleteToken(email);
    }

    @Override
    public boolean hasActiveResetRequest(String email) {
        return resetTokenStorage.hasToken(email) && resetTokenStorage.isTokenActive(email);
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
