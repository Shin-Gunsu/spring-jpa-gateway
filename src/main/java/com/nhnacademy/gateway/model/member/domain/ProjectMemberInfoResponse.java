package com.nhnacademy.gateway.model.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nhnacademy.gateway.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberInfoResponse {
    @JsonProperty("member_id")
    private String id;
    private String email;
    private String status;


    public ProjectMemberInfoResponse(User user, Auth auth) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.status = user.getStatus().toString();
    }
}