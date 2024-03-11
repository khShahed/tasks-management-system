package com.shahed.taskmanagementsystem.jpa.entity;

public enum ETaskStatus {
    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    private final String status;

    ETaskStatus(String status) {
        this.status = status;
    }
}
