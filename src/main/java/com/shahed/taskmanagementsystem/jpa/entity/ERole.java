package com.shahed.taskmanagementsystem.jpa.entity;

public enum ERole {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private final String role;

    ERole(String role) {
        this.role = role;
    }
}
