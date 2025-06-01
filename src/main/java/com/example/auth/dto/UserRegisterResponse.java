package com.example.auth.dto;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;


import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserRegisterResponse {


    private UUID id;


    private String numberPhone;


    private String email;


    private String password;


    private String firstname;


    private LocalDateTime createdAt;

    @JsonIgnore
    private String accessToken;

    @JsonIgnore
    private String refreshToken;


    public UserRegisterResponse withoutTokens() {
        return UserRegisterResponse.builder()
                .id(this.id)
                .email(this.email)
                .createdAt(this.createdAt)
                .firstname(this.firstname)
                .numberPhone(this.numberPhone)
                .build();
    }
}
