package com.nhnacademy.gateway.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class User {
    private String id;
    private String email;
    @Setter
    private String password;
    private String status;

    public User(){

    }
    public User(String id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }


}
