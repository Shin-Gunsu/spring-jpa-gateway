package com.nhnacademy.gateway.model.task.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TaskIdOnlyRequest {
    @JsonProperty("task_id")
    private final Long id;
}
