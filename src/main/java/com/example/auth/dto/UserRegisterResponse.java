package com.example.auth.dto;


import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserRegisterResponse {


    private UUID uuid;


    private String numberPhone;


    private String email;


    private String password;


    private String firstname;


    private LocalDateTime createdAt;

    private String accessToken;

    private String refreshToken;
}
