package com.example.auth.service.register;


import com.example.auth.exception.DuplicateEmailException;
import com.example.auth.exception.DuplicateNumberPhoneException;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserValidationService {

    private final UserRepository userRepository;

    public void checkPhoneNumberUniqueness (String numberPhone){
        if (userRepository.existsByNumberPhone(numberPhone)){
            log.error("Попытка зарегистрировать пользователя с уже " +
                    "использованным номером телефона: {}", numberPhone);
            throw new DuplicateNumberPhoneException(
                    "Этот номер телефона уже используется"
            );
        }
    }

    public void checkEmailUniqueness(String email){
        if (userRepository.existsByEmail(email)){
            log.error("Попытка зарегистрировать пользователя с уже " +
                    "использованной электронной почтой: {}",email);
            throw new DuplicateEmailException(
                    "Эта электронная почта уже используется"
            );
        }
    }
}
