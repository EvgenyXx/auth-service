package com.example.auth.service.register;

import com.example.auth.dto.AuthTokens;
import com.example.auth.dto.UserRegisterRequest;
import com.example.auth.dto.UserRegisterResponse;
import com.example.auth.dto.mapper.KafkaEventMapper;
import com.example.auth.dto.mapper.UserMapper;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.jwt.JwtService;
import com.example.auth.service.role.RoleService;
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
    private final RoleService roleService;
    private final KafkaEventMapper kafkaEventMapper;

    @Override
    @Transactional
    public UserRegisterResponse registerUser(UserRegisterRequest userRegisterRequest) {
        userValidationService.checkEmailUniqueness(userRegisterRequest.getEmail());
        userValidationService.checkPhoneNumberUniqueness(userRegisterRequest.getNumberPhone());
        User user = userMapper.toRegisterEntity(userRegisterRequest);
        user.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        user.setRoles(List.of(roleService.getDefaultUserRole()));
        User saveUser = userRepository.saveAndFlush(user);
        sendUserRegistrationEvent(user);
        AuthTokens authTokens = generateAuthTokens(saveUser);

        return buildResponse(user, authTokens);
    }


    private void sendUserRegistrationEvent(User user) {
        try {
            UserRegistrationNotification event = kafkaEventMapper.toAvro(user);
            kafkaTemplate.send("user-registration-topic", event)
                    .whenComplete((result,ex) -> {
                        if (ex != null) {
                            log.error("Ошибка отправки в Kafka для пользователя ID: {}", user.getId(), ex);
                        } else {
                            log.info("Событие отправлено для пользователя ID: {}", user.getId());
                        }
                    });
        } catch (Exception e) {
            log.error("Ошибка события для пользователя ID: {}", user.getId(), e);
        }
    }

    private AuthTokens generateAuthTokens(User user) {
        List<String> role = user.getRoles().stream()
                .map(Role::getName).toList();
        return AuthTokens.builder()
                .accessToken(jwtService.generateAccessToken(
                        user.getEmail(),
                        role,
                        user.getId().toString()
                ))
                .refreshToken(jwtService.generateRefreshToken(user.getId().toString()))
                .build();
    }

    private UserRegisterResponse buildResponse(User user, AuthTokens authTokens) {
        return UserRegisterResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .firstname(user.getFirstname())
                .numberPhone(user.getNumberPhone())
                .accessToken(authTokens.getAccessToken())
                .build();
    }


}