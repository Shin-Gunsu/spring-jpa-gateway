package com.nhnacademy.gateway.model.comment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommentCreateCommand {
    private final String content;

}
