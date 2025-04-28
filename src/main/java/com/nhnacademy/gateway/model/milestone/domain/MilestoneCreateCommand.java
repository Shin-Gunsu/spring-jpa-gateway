package com.nhnacademy.gateway.model.milestone.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
@AllArgsConstructor
public class MilestoneCreateCommand {
    private final String name;
    @JsonProperty("start_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final ZonedDateTime startDate;
    @JsonProperty("end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private final ZonedDateTime endDate;
}
