package com.nhnacademy.gateway.model.task.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TaskListResponse {
    private Long id;
    private String name;
}