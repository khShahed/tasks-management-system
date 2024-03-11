package com.shahed.taskmanagementsystem.jpa.entity;

public enum ETaskPriority {
    LOW("Low"),
    MEDIUM("Medium"),
    HIGH("High");

    private final String priority;

    ETaskPriority(String priority) {
        this.priority = priority;
    }
}
