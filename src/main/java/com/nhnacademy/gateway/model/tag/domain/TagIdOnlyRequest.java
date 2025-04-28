package com.nhnacademy.gateway.model.tag.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TagIdOnlyRequest {
    @JsonProperty("tag_id")
    private long tagId;
}