package com.nhnacademy.gateway.model.milestone.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
public class Milestone {
    private final long id;
    private long projectId;
    private String name;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;
}
