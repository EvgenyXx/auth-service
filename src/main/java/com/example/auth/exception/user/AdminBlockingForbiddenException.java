package com.example.auth.exception.user;

public class AdminBlockingForbiddenException extends RuntimeException {

    public AdminBlockingForbiddenException(String message) {
        super(message);
    }
}
