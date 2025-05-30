package com.example.auth.service.register;

import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface UserRegistrationService {

    UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest) throws ExecutionException, InterruptedException, TimeoutException;
}
