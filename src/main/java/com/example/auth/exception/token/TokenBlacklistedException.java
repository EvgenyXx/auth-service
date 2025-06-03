package com.example.auth.exception;

public class TokenBlacklistedException extends RuntimeException {

    public TokenBlacklistedException(String message) {
        super(message);
    }
}
