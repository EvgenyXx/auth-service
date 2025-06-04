package com.example.auth.service.register;

import com.example.auth.dto.LoginRequest;
import com.example.auth.dto.LoginResponse;
import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;



public interface UserRegistrationService {

    UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest) ;

    LoginResponse login (LoginRequest loginRequest);
}
