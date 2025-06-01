package com.example.auth.service.jwt;

import com.example.auth.dto.AuthTokens;
import com.example.auth.entity.User;

public interface AuthTokenService {

    AuthTokens generateAuthTokens(User user);
}
