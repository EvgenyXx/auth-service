package com.example.auth.exception.token;


public class ActiveResetRequestException extends RuntimeException {

    public ActiveResetRequestException(String message) {
        super(message);
    }
}
