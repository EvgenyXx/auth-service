package com.example.auth.service.register;

import com.example.auth.dto.AuthTokens;
import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;
import com.example.auth.dto.mapper.UserMapper;
import com.example.auth.entity.User;
import com.example.auth.service.jwt.AuthTokenService;
import com.example.auth.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;




@Service
@RequiredArgsConstructor
@Log4j2
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserService userService;
    private final UserValidationService userValidationService;
    private final AuthTokenService authTokenService;
    private final UserMapper userMapper;


    @Override
    @Transactional
    public UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest) {
        userValidationService.validateUserData(
                userRegisterRequest.getEmail(),
                userRegisterRequest.getNumberPhone()
        );
        User user = userService.createUser(userMapper.toRegisterEntity(userRegisterRequest));
        AuthTokens authTokens = authTokenService.generateAuthTokens(user);
        return userMapper.toRegisterResponse(user, authTokens);
    }


}