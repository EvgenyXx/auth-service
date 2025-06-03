package com.example.auth.service.user;

public class UserServiceImpl implements UserService {
    @Override
    public boolean existsByEmail(String email) {
        return false;
    }

    @Override
    public boolean existsByNumberPhone(String numberPhone) {
        return false;
    }
}
