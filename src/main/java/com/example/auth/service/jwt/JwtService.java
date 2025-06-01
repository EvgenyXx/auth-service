package com.example.auth.service.jwt;


import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

@Service
public class JwtService {


    private final String secretKey;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;

    public JwtService(
            @Value("${spring.security.jwt.secret}") String secretKey,
            @Value("${spring.security.jwt.expiration.access}") Duration accessTokenExpiration,
            @Value("${spring.security.jwt.expiration.refresh}") Duration refreshTokenExpiration
    ) {
        if (secretKey == null || secretKey.isBlank()) {
            throw new IllegalArgumentException("JWT secret key is not configured!");
        }
        this.secretKey = secretKey;
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String generateAccessToken(String userId, List<String> roles, String email) {
        return Jwts.builder()
                .subject(userId)
                .claims(Map.of(
                        "roles", roles,
                        "email", email // опционально
                ))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration.toMillis()))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .claim("jti", UUID.randomUUID().toString()) // Уникальный ID токена
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration.toMillis()))
                .signWith(getSecretKey())
                .compact();
    }


    private Key getSecretKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }


}
