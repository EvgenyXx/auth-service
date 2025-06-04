package com.example.auth.service.user;




import com.example.auth.entity.User;
import com.example.auth.event.UserCreatedEvent;
import com.example.auth.exception.user.UserNotFoundException;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.role.RoleService;
import com.example.auth.util.PhoneNormalizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ApplicationEventPublisher applicationEventPublisher;



    @Override
    public User createUser(User user) {
        user.setRoles(List.of(roleService.getDefaultUserRole()));
        user.setNumberPhone(PhoneNormalizer.normalize(user.getNumberPhone()));
        User saveUser = userRepository.saveAndFlush(user);
        applicationEventPublisher.publishEvent(new UserCreatedEvent(saveUser));
        return saveUser;
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByNumberPhone(String numberPhone) {
        return userRepository.existsByNumberPhone(numberPhone);
    }



    @Override
    public User findById(UUID userId) {
        return userRepository.findById(userId).orElseThrow(
                ()-> new UserNotFoundException("Пользователь не найден")
        );
    }

    @Override
    public User findByNumberPhone(String numberPhone) {
        return userRepository.findByNumberPhone(numberPhone)
                .orElseThrow(()-> new UserNotFoundException(
                        "Неверные учетные данные !"
                ));
    }
}
