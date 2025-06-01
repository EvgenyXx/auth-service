package com.example.auth.service.register;

import com.example.auth.dto.AuthTokens;
import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;
import com.example.auth.dto.mapper.UserMapper;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.jwt.AuthTokenService;
import com.example.auth.service.kafka.KafkaEventSender;
import com.example.auth.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Log4j2
public class UserRegistrationServiceImpl implements UserRegistrationService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidationService userValidationService;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private  final KafkaEventSender kafkaEventSender;
    private final AuthTokenService authTokenService;


    @Override
    @Transactional
    public UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest) {
        userValidationService.checkEmailUniqueness(userRegisterRequest.getEmail());
        userValidationService.checkPhoneNumberUniqueness(userRegisterRequest.getNumberPhone());
        User user = userMapper.toRegisterEntity(userRegisterRequest);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        user.setRoles(List.of(roleService.getDefaultUserRole()));
        User saveUser = userRepository.saveAndFlush(user);
        kafkaEventSender.sendUserRegistrationEvent(user);
        AuthTokens authTokens = authTokenService.generateAuthTokens(saveUser);
        return userMapper.toRegisterResponse(saveUser,authTokens);
    }




}