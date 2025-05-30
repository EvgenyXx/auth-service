package com.example.auth.service.register;

import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;
import com.example.auth.dto.mapper.UserMapper;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.jwt.JwtService;
import com.example.notification.UserRegistrationNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, UserRegistrationNotification> kafkaTemplate;
    private final JwtService jwtService;

    @Override
    @Transactional
    public UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest) {
        userValidationService.checkEmailUniqueness(userRegisterRequest.getEmail());
        userValidationService.checkPhoneNumberUniqueness(userRegisterRequest.getNumberPhone());
        User user = userMapper.toRegisterEntity(userRegisterRequest);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        User saveUser = userRepository.save(user);
        sendUserRegistrationEvent(saveUser);
        UserRegisterResponse userRegisterResponse = userMapper.toRegisterDto(saveUser);
        userRegisterResponse.setAccessToken(accessToken(saveUser));
        userRegisterResponse.setRefreshToken(refreshToken(saveUser.getId().toString()));
        return userRegisterResponse;
    }

    private String accessToken(User user) {
        List<String> role = user.getRoles().stream()
                .map(Role::getName).toList();
        return jwtService.generateAccessToken(
                user.getId().toString(),
                role,
                user.getEmail()
        );
    }

    private String refreshToken(String userId) {
        return jwtService.generateRefreshToken(userId);
    }

    private void sendUserRegistrationEvent(User user)  {
        UserRegistrationNotification event = UserRegistrationNotification.newBuilder()
                .setUserId(user.getId().toString())
                .setRegistrationDate(user.getCreatedAt().toString())
                .setEmail(user.getEmail())
                .setFirstname(user.getFirstname())
                .setNumberPhone(user.getNumberPhone())
                .build();
        kafkaTemplate.send("user-registration-topic", event);
        log.info("Отправлен топик: {}", event);

    }
}
