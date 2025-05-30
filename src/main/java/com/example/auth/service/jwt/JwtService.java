package com.example.auth.service.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class JwtService {

    private static final String SECRET_KEY = "bNDoES6IHyt9YhNeF7GpFj1hRAhO0jDhXf3Dnk4fZl4=";



    public String generateAccessToken(String userId, List<String> roles,String email) {
        return Jwts.builder()
                .subject(userId)
                .claims(Map.of(
                        "roles", roles,
                        "email", email // опционально
                ))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 30 * 60 * 1000)) // 30 мин
                .signWith(getSecretKey())
                .compact();
    }

    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .claim("jti", UUID.randomUUID().toString()) // Уникальный ID токена
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)) // 30 дней
                .signWith(getSecretKey())
                .compact();
    }



    private Key getSecretKey() {
        byte[]keyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }



}
