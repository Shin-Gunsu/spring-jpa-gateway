package com.nhnacademy.gateway.model.milestone.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MilestoneDetailResponse {
    private final long id;
    private String name;
    private String startDate;
    private String endDate;
}
