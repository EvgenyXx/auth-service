package com.example.auth.event;

import com.example.auth.entity.User;

import java.time.Instant;

public record PasswordResentEvent(
        User user,
        String token,
        Instant expirationTime,
        String resetLinkTemplate
) {
}
