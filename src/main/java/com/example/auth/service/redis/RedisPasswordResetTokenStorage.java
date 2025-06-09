package com.example.auth.service.redis;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisPasswordResetTokenStorage {



    @Value("${app.redis.prefixes.email}")
    private String emailPrefix;


    @Value("${app.redis.prefixes.token}")
    private String tokenPrefix;


    @Value("${app.redis.tels.password-reset}")
    private Duration tokenTtl;

    private final RedisTemplate<String, String> redisTemplate;


    public void saveToken(String email, String token) {

        String oldToken = redisTemplate.opsForValue().get(emailPrefix + email);
        if (oldToken != null) {
            redisTemplate.delete(tokenPrefix + oldToken);
        }
        redisTemplate.opsForValue().set(tokenPrefix + token, email, tokenTtl);
        redisTemplate.opsForValue().set(emailPrefix + email, token, tokenTtl);

    }


    public String getEmailByToken(String token) {
        String key = tokenPrefix + token;
        return redisTemplate.opsForValue().get(key);
    }


    public void deleteToken(String email) {
        String key = tokenPrefix + email;
        redisTemplate.delete(key);
    }

    public Instant getDefaultExpireTime(){
        return Instant.now().plus(tokenTtl);
    }

    public boolean isTokenValid(String email,String tokenToCheck){
        String storedToken = getEmailByToken(email);
        return  storedToken.equals(tokenToCheck);
    }

    public boolean isTokenExpired(String email) {
        String key = tokenPrefix + email;
        return  redisTemplate.getExpire(key) <= 0;
    }





}
