package com.example.auth.config;


import lombok.Getter;
import lombok.Setter;

import org.springframework.boot.context.properties.ConfigurationProperties;



import java.time.Duration;


@Getter
@Setter
@ConfigurationProperties(prefix = "spring.security.jwt.cookie")
public class JwtCookieProperties {

    private String name;
    private String path;
    private boolean secure;
    private boolean httpOnly;
    private String sameSite;
    private Duration maxAge;


}

