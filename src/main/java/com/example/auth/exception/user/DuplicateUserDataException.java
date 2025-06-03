package com.example.auth.exception;


import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
public class DuplicateUserDataException extends RuntimeException implements Serializable {

    private final List<ConflictField> conflictFields;

    public DuplicateUserDataException(List<ConflictField> conflictFields) {
        super("Учетная запись с такими данными уже существует");
        this.conflictFields = conflictFields;
    }

    public record ConflictField(String field, String message) implements Serializable {}
}


