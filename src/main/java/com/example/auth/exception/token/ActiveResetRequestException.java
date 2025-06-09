package com.example.auth.exception.token;


//TODO добавь исключение в общий обработчик
public class ActiveResetRequestException extends RuntimeException {

    public ActiveResetRequestException(String message) {
        super(message);
    }
}
