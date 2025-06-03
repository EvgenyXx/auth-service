package com.example.auth.exception.user;


import com.example.auth.exception.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import java.util.List;



@RestControllerAdvice
public class UserExceptionHandler {
    private static final String PATH = "uri=";
    private static final String USER_NOT_FOUND = "User not found";

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

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .message("Validation failed")
                .path(request.getDescription(false).replace(PATH, ""))
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(apiError);
    }



    @ExceptionHandler(DuplicateUserDataException.class)
    public ResponseEntity<ApiError> handleDuplicateUserDataException(
            DuplicateUserDataException ex,
            WebRequest request) {

        List<ApiError.ConflictField> apiConflictFields = ex.getConflictFields().stream()
                .map(conflict -> new ApiError.ConflictField(
                        conflict.field(),
                        conflict.message()
                        )
                )
                .toList();

        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error(HttpStatus.CONFLICT.getReasonPhrase())
                .message(ex.getMessage())  // Используем сообщение из исключения
                .path(request.getDescription(false).replace(PATH, ""))
                .conflicts(apiConflictFields)

                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiError);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError>handleUserNotFound(UserNotFoundException e,
                                                      WebRequest webRequest){
        ApiError apiError = ApiError.builder()
                .timestamp(LocalDateTime.now())
                .error(USER_NOT_FOUND)
                .status(HttpStatus.NOT_FOUND.value())
                .path(webRequest.getDescription(false).replace(PATH,""))
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(apiError,HttpStatus.NOT_FOUND);
    }



}
