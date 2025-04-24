package com.nhnacademy.gateway.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserLoginRequest {
    private String id;
    private String password;

}
