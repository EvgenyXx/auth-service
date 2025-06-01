package com.example.auth.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.stereotype.Component;



import java.time.Duration;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.jwt.cookie")
public class JwtCookieProperties {
    private String name = "refresh_token";
    private String path = "/api/v1/auth";
    private boolean secure = true;
    private boolean httpOnly = true;
    private String sameSite = "Strict";
    private Duration maxAge = Duration.ofDays(30);


}

