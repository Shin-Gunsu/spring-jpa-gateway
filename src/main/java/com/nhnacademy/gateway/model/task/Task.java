package com.nhnacademy.gateway.model.task;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Task {
    private final long id;
    private long projectId;
    private long milestoneId;
}
