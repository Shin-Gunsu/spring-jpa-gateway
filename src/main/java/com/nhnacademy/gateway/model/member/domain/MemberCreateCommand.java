package com.nhnacademy.gateway.model.member.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberCreateCommand {
    @JsonProperty("member_id")
    private String memberId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Auth auth;
}