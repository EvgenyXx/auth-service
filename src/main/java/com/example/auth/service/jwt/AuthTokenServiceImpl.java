package com.example.auth.service.jwt;

import com.example.auth.dto.AuthTokens;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.exception.token.ExpiredTokenException;
import com.example.auth.exception.token.TokenBlacklistedException;
import com.example.auth.service.redis.TokenRedisBlacklistService;
import com.example.auth.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final JwtService jwtService;
    private final UserService userService;
    private final TokenRedisBlacklistService tokenRedisBlacklistService;




    @Override
    public AuthTokens generateAuthTokens(User user) {
        List<String> role = extractRoles(user);
        return AuthTokens.builder()
                .accessToken(jwtService.generateAccessToken(
                        user.getId().toString(),
                        role,
                        user.getEmail()
                ))
                .refreshToken(jwtService.generateRefreshToken(user.getId().toString()))
                .build();
    }

    private List<String> extractRoles(User user){
        return user.getRoles().stream()
                .map(Role::getName)
                .toList();
    }

    @Override
    public AuthTokens refreshToken(String refreshToken) {
        // 1. Проверяем, не отозван ли уже токен
        if (tokenRedisBlacklistService.isTokenBlacklisted(refreshToken)) {
            throw new TokenBlacklistedException("Токен отозван");
        }
        // 2. Проверяем валидность токена
        String userId = jwtService.extractSubject(refreshToken);
        if (jwtService.isTokenExpired(refreshToken)) {
            throw new ExpiredTokenException("Токен истёк");
        }
        // 3. Получаем пользователя
        User user = userService.findById(UUID.fromString(userId));
        tokenRedisBlacklistService.removeFromBlacklist(refreshToken);
        // 4. Добавляем старый токен в черный список
        tokenRedisBlacklistService.addToBlacklist(refreshToken, Duration.ofDays(30));

        // 5. Генерируем новые токены
        return generateAuthTokens(user);
    }
}
