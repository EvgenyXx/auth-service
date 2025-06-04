package com.example.auth.exception.token;

import com.example.auth.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;



import static com.example.auth.exception.ApiError.buildErrorResponse;

@RestControllerAdvice
public class TokenExceptionHandler {


    @ExceptionHandler(TokenBlacklistedException.class)
    public ResponseEntity<ApiError>handleTokenBlackListException(TokenBlacklistedException e,
                                                        WebRequest webRequest){
        return buildErrorResponse(
                e.getMessage(),
                webRequest,
                HttpStatus.UNAUTHORIZED,
                null,
                null
        );
    }


    @ExceptionHandler(ExpiredTokenException.class)
    public ResponseEntity<ApiError>handleExpiredToken(ExpiredTokenException e,
                                                        WebRequest webRequest){
        return buildErrorResponse(
                e.getMessage(),
                webRequest,
                HttpStatus.UNAUTHORIZED,
                null,
                null
        );
    }


}
