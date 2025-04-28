package com.nhnacademy.gateway.model.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private String id;
    @Setter
    private String password;
    private String email;

    @JsonProperty("role")
    @JsonSerialize(using = ToStringSerializer.class)
    private Status status;

    public User(){

    }

}
