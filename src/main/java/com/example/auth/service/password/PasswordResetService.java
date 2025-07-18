package com.example.auth.service.password;

import com.example.auth.dto.ForgotPasswordRequest;
import com.example.auth.dto.PasswordResetRequest;

import java.util.UUID;

public interface PasswordResetService {

    /**
     * Запрашивает сброс пароля для указанного email.
     * Генерирует токен, сохраняет его в Redis и отправляет письмо со ссылкой.
     *
     * @param request Email пользователя.
     * @throws IllegalArgumentException если пользователь не найден.
     */
    void requestPasswordReset(ForgotPasswordRequest request);


    /**
     * Устанавливает новый пароль пользователя, если токен валиден.
     *

     * @param request Объект запроса на сброс пароля, содержащий:
     *                - токен сброса пароля
     *                - новый пароль
     * @throws IllegalArgumentException если токен недействителен.
     */
    void resetPassword(PasswordResetRequest request);


    /**
     * Проверяет, есть ли активный запрос на сброс пароля для email.
     *
     * @param email Email пользователя.
     * @return true, если запрос существует, иначе false.
     */
    boolean hasActiveResetRequest(String email);

    /**
     * Генерирует уникальный токен для сброса пароля.
     * (Может быть приватным, но вынесен в интерфейс для гибкости.)
     *
     * @return Сгенерированный токен.
     */
    default String generateToken() {
        return UUID.randomUUID().toString();
    }

    /**
     * Формирует ссылку для сброса пароля.
     * (Может быть приватным, но вынесен в интерфейс для кастомизации.)
     *
     * @param email Email пользователя.
     * @param token Токен сброса.
     * @return Ссылка для сброса пароля.
     */
    default String generateResetLink(String email, String token) {
        return "https://your-app.com/reset-password?token=" + token + "&email=" + email;
    }
}