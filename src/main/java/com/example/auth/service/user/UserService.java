package com.example.auth.service.user;


import com.example.auth.entity.User;

import java.util.UUID;

public interface UserService {

    /*
    Методы для UserValidationService.java
     */
    boolean existsByEmail(String email);

    boolean existsByNumberPhone(String numberPhone);




    User findById(UUID userId);

    User createUser(User user);

    User findByNumberPhone(String numberPhone);


}
