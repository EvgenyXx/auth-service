package com.example.auth.exception.user;


import com.example.auth.exception.InvalidCredentialsException;
import com.example.auth.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;


import java.util.List;

import static com.example.auth.exception.ApiError.*;


@RestControllerAdvice
public class UserExceptionHandler {



    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        List<ApiError.FieldValidationError> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ApiError.FieldValidationError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .rejectedValue(error.getRejectedValue())
                        .build())
                .toList();

        return buildErrorResponse(
                DEFAULT_VALIDATION_ERROR,
                request,
                HttpStatus.BAD_REQUEST,
                null,
                fieldErrors
        );
    }



    @ExceptionHandler(DuplicateUserDataException.class)
    public ResponseEntity<ApiError> handleDuplicateUserDataException(
            DuplicateUserDataException ex,
            WebRequest request) {

        List<ApiError.ConflictField> apiConflictFields = ex.getConflictFields().stream()
                .map(conflict -> new ApiError.ConflictField(
                        conflict.field(),
                        conflict.message())).toList();

        return buildErrorResponse(
                DUPLICATE_USER_DATA,
                request,
                HttpStatus.CONFLICT,
                apiConflictFields,
                null
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError>handleUserNotFound(UserNotFoundException e, WebRequest webRequest){
        return buildErrorResponse(
                e.getMessage(),
                webRequest,
                HttpStatus.NOT_FOUND,
                null,
                null
        );
    }

    @ExceptionHandler(AccountBlockedException.class)
    public ResponseEntity<ApiError>handleAccountBlocked(AccountBlockedException e, WebRequest webRequest){
        return buildErrorResponse(
                e.getMessage(),
                webRequest,
                HttpStatus.LOCKED,
                null,
                null
        );
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiError>handleInvalidCredentials(InvalidCredentialsException e, WebRequest webRequest){
        return buildErrorResponse(
                e.getMessage(),
                webRequest,
                HttpStatus.UNAUTHORIZED,
                null,
                null
        );
    }






}
