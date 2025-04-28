package com.nhnacademy.gateway.model.comment.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class CommentResponse {
    private final long id;
    @JsonProperty("member_id")
    private final String memberId;
    private final String content;
    @JsonProperty("created_at")
    private final String createdAt;
}
