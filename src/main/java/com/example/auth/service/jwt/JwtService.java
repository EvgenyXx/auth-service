package com.example.auth.service.jwt;

import com.example.auth.config.JwtKeyProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class JwtService {

    private final PrivateKey privateKey;
    private final PublicKey publicKey;
    private final Duration accessTokenExpiration;
    private final Duration refreshTokenExpiration;
    private final String issuer;

    public JwtService(
            JwtKeyProvider keyProvider,
            @Value("${spring.security.jwt.expiration.access}") Duration accessTokenExpiration,
            @Value("${spring.security.jwt.expiration.refresh}") Duration refreshTokenExpiration,
            @Value("${spring.security.jwt.issuer-uri}") String issuer
    ) {
        this.privateKey = keyProvider.getPrivateKey();
        this.publicKey = keyProvider.getRsaPublicKey();
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
        this.issuer = issuer;
    }

    public String generateAccessToken(String userId, List<String> roles, String email) {
        return Jwts.builder()
                .subject(userId)
                .claims(Map.of(
                        "roles", roles,
                        "email", email
                ))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration.toMillis()))
                .issuer(issuer)
                .signWith(privateKey)
                .compact();
    }

    public String generateRefreshToken(String userId) {
        return Jwts.builder()
                .subject(userId)
                .claim("jti", UUID.randomUUID().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration.toMillis()))
                .issuer(issuer)
                .signWith(privateKey)
                .compact();
    }

    public boolean isTokenExpired(String token) {
        return !extractAllClaims(token).getExpiration().after(new Date());
    }

    //TODO пересмотреть правильность
    public String extractSubject(String token) {
        return extractAllClaims(token).getSubject();
    }



    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}