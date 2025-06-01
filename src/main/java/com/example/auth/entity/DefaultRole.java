package com.example.auth.entity;

import lombok.Getter;

@Getter
public enum DefaultRole {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private final String name;

    DefaultRole(String name) {
        this.name = name;
    }
}
