package com.example.auth.service.redis;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPasswordResetTokenStorage {


    @Value("${app.redis.prefixes.email}")
    private String emailPrefix;


    @Value("${app.redis.prefixes.token}")
    private String tokenPrefix;


    @Value("${app.redis.ttl.password-reset}")
    private Duration tokenTtl;

    private final RedisTemplate<String, String> redisTemplate;


    public void saveToken(String email, String token) {

        String oldToken = redisTemplate.opsForValue().get(emailPrefix + email);
        if (oldToken != null) {
            redisTemplate.delete(tokenPrefix + oldToken);
        }
        redisTemplate.opsForValue().set(tokenPrefix + token, email, tokenTtl);
        redisTemplate.opsForValue().set(emailPrefix + email, token, tokenTtl);
        log.debug("Saving token for email: {}", email);
    }


    public String getEmailByToken(String token) {
        String key = tokenPrefix + token;
        return redisTemplate.opsForValue().get(key);
    }


    public void deleteToken(String email) {
        String token = redisTemplate.opsForValue().get(emailPrefix + email);
        if (token != null) {
            redisTemplate.delete(tokenPrefix + token);
        }
        redisTemplate.delete(emailPrefix + email);
    }

    public Instant getDefaultExpireTime() {
        return Instant.now().plus(tokenTtl);
    }

    public boolean isTokenValid(String tokenToCheck) {
        String storedEmail = getEmailByToken(tokenToCheck);
        if (storedEmail == null) {
            return false;
        }
        String storedToken = redisTemplate.opsForValue().get(emailPrefix + storedEmail);
        if (!Objects.equals(tokenToCheck, storedToken)) {
            return false;
        }
        return isTokenActive(storedEmail);
    }

    public boolean isTokenActive(String email) {
        Long expire = redisTemplate.getExpire(emailPrefix + email);
        return expire != null && (expire > 0 || expire == -1);
    }

    public boolean hasToken(String email) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(emailPrefix + email));
    }


}
