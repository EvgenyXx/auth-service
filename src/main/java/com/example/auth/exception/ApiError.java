package com.example.auth.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiError {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;  // Общее сообщение
    private String path;
    private List<FieldValidationError> fieldErrors; // Специфичные ошибки полей
    private List<ConflictField>conflicts;

    @Data
    @Builder
    @AllArgsConstructor
    public static class FieldValidationError {
        private String field;
        private String message;
        private Object rejectedValue;
    }

    @Data
    @Builder
    @AllArgsConstructor
    public static class ConflictField {
        private String field;
        private String message;


    }
}