package com.nhnacademy.gateway.model.project.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProjectStatusUpdateCommand {
    private ProjectStatus status;
}
