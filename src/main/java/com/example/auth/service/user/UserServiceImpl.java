package com.example.auth.service.user;




import com.example.auth.entity.User;
import com.example.auth.event.UserCreatedEvent;
import com.example.auth.exception.user.UserNotFoundException;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.role.RoleService;
import com.example.auth.util.PhoneNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private static final String USER_NOT_FOUND = "User not found";

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final PasswordEncoder passwordEncoder;



    @Override
    @Transactional
    public User createUser(User user) {
        user.setRoles(Set.of(roleService.getDefaultUserRole()));
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
                ()-> new UserNotFoundException(USER_NOT_FOUND)
        );
    }

    @Override
    public User findByNumberPhone(String numberPhone) {
        return userRepository.findByNumberPhone(numberPhone)
                .orElseThrow(()-> new UserNotFoundException(
                        "Неверные учетные данные !"
                ));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException(USER_NOT_FOUND));
    }

    @Override
    public void updatePassword(String email, String newPassword) {
       User user = findByEmail(email);
       String passwordUpdate = passwordEncoder.encode(newPassword);
       user.setPassword(passwordUpdate);
       userRepository.save(user);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }
}
