package com.nhnacademy.gateway.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserLoginRequest {
    private String userId;
    private String password;

}
