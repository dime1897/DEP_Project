package com.dime.lsd.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum TaskStatus {
    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    ON_HOLD("On Hold"),
    DONE("Done");

    private final String label;
}
