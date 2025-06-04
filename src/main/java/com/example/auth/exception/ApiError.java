package com.example.auth.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Collections;
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

    private static final String PATH_PREFIX = "uri=";
    public static final String DEFAULT_VALIDATION_ERROR = "Ошибка валидации данных";
    public static final String DUPLICATE_USER_DATA = "Дублирование данных";

    public static ResponseEntity<ApiError> buildErrorResponse(
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
                .path(request.getDescription(false).replace(PATH_PREFIX, ""))
                .conflicts(conflicts != null ? conflicts : Collections.emptyList())
                .fieldErrors(fieldErrors != null ? fieldErrors : Collections.emptyList())
                .build();

        return ResponseEntity.status(status).body(apiError);
    }
}