package com.example.auth.exception.token;

//TODO ДОБАВИТЬ В ОБЩИЙ ОБРАБОТЧИК
public class InvalidTokenException extends RuntimeException {

    public InvalidTokenException(String message) {
        super(message);
    }
}
