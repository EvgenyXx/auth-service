package com.example.auth.exception.token;

public class TokenBlacklistedException extends RuntimeException {

    public TokenBlacklistedException(String message) {
        super(message);
    }
}
