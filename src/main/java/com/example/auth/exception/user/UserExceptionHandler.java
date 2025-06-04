package com.example.auth.exception.user;


import com.example.auth.dto.InvalidCredentialsException;
import com.example.auth.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import java.util.Collections;
import java.util.List;



@RestControllerAdvice
public class UserExceptionHandler {
    private static final String PATH = "uri=";


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
                "Ошибка валидации данных",
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
                ex.getMessage(),
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

    private ResponseEntity<ApiError> buildErrorResponse(
            String message,
            WebRequest request,
            HttpStatus status,
            List<ApiError.ConflictField> conflicts,
            List<ApiError.FieldValidationError> fieldErrors) {

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(status.getReasonPhrase())
                .message(message)
                .path(request.getDescription(false).replace(PATH, ""))
                .conflicts(conflicts != null ? conflicts : Collections.emptyList())
                .fieldErrors(fieldErrors != null ? fieldErrors : Collections.emptyList())
                .build();

        return ResponseEntity.status(status).body(apiError);
    }




}
