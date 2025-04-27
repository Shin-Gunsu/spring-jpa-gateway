package com.nhnacademy.gateway.model.task.domain;

import lombok.Getter;

@Getter
public class TaskCreateCommand {
    private final String name;

    public TaskCreateCommand(String name) {
        this.name = name;
    }
}
