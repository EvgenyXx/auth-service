package com.example.auth.service.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;


@Service
@RequiredArgsConstructor
public class TokenBlacklistService {
    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLACKLIST_PREFIX = "bl:";

    // Добавить токен в чёрный список
    public void addToBlacklist(String token, Duration ttl) {
        redisTemplate.opsForValue().set(
                BLACKLIST_PREFIX + token,
                "revoked",
                ttl
        );
    }

    // Проверить наличие токена в чёрном списке
    public boolean isTokenBlacklisted(String refreshToken) {
        if (redisTemplate == null) {
            throw new IllegalStateException("RedisTemplate is not initialized!");
        }
        return Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + refreshToken));

    }

    // Удалить токен из чёрного списка
    public void removeFromBlacklist(String token) {
        redisTemplate.delete(BLACKLIST_PREFIX + token);
    }
}
