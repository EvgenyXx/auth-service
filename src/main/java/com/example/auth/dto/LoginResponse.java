package com.example.auth.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LoginResponse {

    private String numberPhone;


    private String email;

    private String firstname;


    private LocalDateTime createdAt;

    @JsonIgnore
    private String accessToken;

    @JsonIgnore
    private String refreshToken;
}
