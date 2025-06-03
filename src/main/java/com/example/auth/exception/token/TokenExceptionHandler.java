package com.example.auth.exception.token;

import com.example.auth.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

@RestControllerAdvice
public class TokenExceptionHandler {

    private static final String PATH = "uri=";
    public static final String TOKEN_REVOKED = "Token revoked";

    public static final String TOKEN_EXPIRED = "Token expired";





    @ExceptionHandler(TokenBlacklistedException.class)
    public ResponseEntity<ApiError>handleTokenBlackListException(TokenBlacklistedException e,
                                                        WebRequest webRequest){
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .error(TOKEN_REVOKED)
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(webRequest.getDescription(false).replace(PATH,""))
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ApiError>handleExpiredToken(ExpiredTokenException e,
                                                        WebRequest webRequest){
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .error(TOKEN_EXPIRED)
                .status(HttpStatus.UNAUTHORIZED.value())
                .path(webRequest.getDescription(false).replace(PATH,""))
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(apiError,HttpStatus.UNAUTHORIZED);
    }


}
