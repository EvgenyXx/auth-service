package com.example.auth.service.jwt;

import com.example.auth.dto.AuthTokens;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {
    private final JwtService jwtService;


    @Override
    public AuthTokens generateAuthTokens(User user) {
        List<String> role = extractRoles(user);
        return AuthTokens.builder()
                .accessToken(jwtService.generateAccessToken(
                        user.getEmail(),
                        role,
                        user.getId().toString()
                ))
                .refreshToken(jwtService.generateRefreshToken(user.getId().toString()))
                .build();
    }

    private List<String> extractRoles(User user){
        return user.getRoles().stream()
                .map(Role::getName)
                .toList();
    }
}
