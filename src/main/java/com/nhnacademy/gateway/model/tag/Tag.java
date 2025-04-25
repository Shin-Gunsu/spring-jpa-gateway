package com.nhnacademy.gateway.model.tag;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class Tag {
    private String name;
    private long projectId;
}
