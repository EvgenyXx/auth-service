package com.example.auth.service.jwt;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
                .expiration(new Date(System.currentTimeMillis() +
                        accessTokenExpiration.toMillis()))
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .claim("jti", UUID.randomUUID().toString()) // Уникальный ID токена
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() +
                        refreshTokenExpiration.toMillis()))
                .signWith(getSecretKey())
                .compact();
    }


    private SecretKey getSecretKey() {
        byte[] keyByte = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyByte);
    }

    public boolean isTokenExpired(String token){
        return !extractAllClaims(token)
                .getExpiration().after(new Date());
    }

    public String extractSubject(String token){
        return extractAllClaims(token).getSubject();
    }


    private Claims extractAllClaims(String token){
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }



}
