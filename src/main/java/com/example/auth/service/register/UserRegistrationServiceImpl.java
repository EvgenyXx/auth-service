package com.example.auth.service.register;

import com.example.auth.dto.*;
import com.example.auth.dto.mapper.UserMapper;
import com.example.auth.entity.User;
import com.example.auth.exception.InvalidCredentialsException;
import com.example.auth.service.jwt.AuthTokenService;
import com.example.auth.service.redis.LoginAttemptService;
import com.example.auth.service.user.UserService;
import com.example.auth.service.validation.UserValidationService;
import com.example.auth.util.PhoneNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptService loginAttemptService;


    @Override
    @Transactional
    public UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest) {
        userValidationService.validateUserData(
                userRegisterRequest.getEmail(),
                userRegisterRequest.getNumberPhone()
        );
        User user = userMapper.toRegisterEntity(userRegisterRequest);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));

        User saveUser = userService.createUser(user);
        AuthTokens authTokens = authTokenService.generateAuthTokens(saveUser);
        return userMapper.toRegisterResponse(user, authTokens);
    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {
        String normalizedPhone = PhoneNormalizer.normalize(loginRequest.getNumberPhone());
        loginAttemptService.checkIfBlocked(normalizedPhone);
        User user = userService.findByNumberPhone(normalizedPhone);
        if (user.isBlocked()){
            //TODO сделать исключение и добавить в центральный обработчик
            throw new RuntimeException("Аккаунт заблокирован! Обратитесь в поддержку");
        }
        validatePasswordAndHandleAttempts(loginRequest.getRawPassword(), user,normalizedPhone);
        loginAttemptService.loginSuccess(normalizedPhone);
        AuthTokens authTokens = authTokenService.generateAuthTokens(user);
        return userMapper.toLoginDto(user, authTokens);
    }

    private void validatePasswordAndHandleAttempts(String rawPassword,User user,String normalizedPhone){
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            loginAttemptService.loginFailed(normalizedPhone);
            LoginAttemptService.AttemptStatus newStatus = loginAttemptService.getAttemptsStatus(normalizedPhone);
            throw new InvalidCredentialsException(
                    newStatus.isBlocked()
                            ? "Аккаунт заблокирован после последней попытки"
                            : "Неверный пароль. Осталось попыток: " + newStatus.remainingAttempts()
            );
        }
    }
}