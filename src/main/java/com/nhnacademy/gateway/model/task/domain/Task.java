package com.nhnacademy.gateway.model.task.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
public class Task {
    private Long id;
    private String name;
    private String milestoneName;
    private ZonedDateTime createdAt;
}
