package com.nhnacademy.gateway.model.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
public class Comment {
    private final long id;
    private long taskId;
    private String memberId;
    private String content;
    private ZonedDateTime createdAt;
}
