package com.nhnacademy.gateway.model.milestone.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class MilestoneListResponse {
    private final long id;
    private final String name;
}
