package com.example.auth.service.validation;



import com.example.auth.exception.user.DuplicateUserDataException;
import com.example.auth.service.user.UserService;
import com.example.auth.util.PhoneNormalizer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserValidationService {

    private final UserService userService;

    public void validateUserData(String email, String numberPhone) {
        List<DuplicateUserDataException.ConflictField> conflicts = new ArrayList<>();

        // Проверка email
        if (userService.existsByEmail(email)) {
            conflicts.add(new DuplicateUserDataException.ConflictField("email",
                    "Эта электронная почта уже используется"));
        }

        // Проверка телефона
        String normalNumber = PhoneNormalizer.normalize(numberPhone);
        if (userService.existsByNumberPhone(normalNumber)) {
            conflicts.add(new DuplicateUserDataException.ConflictField("numberPhone",
                    "Этот номер телефона уже используется"));
        }

        if (!conflicts.isEmpty()) {
            throw new DuplicateUserDataException(conflicts);
        }

    }
}
