package com.nhnacademy.gateway.model.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Project {
    private final long id;
    private String name;
    private ProjectStatus statusCode;
}
