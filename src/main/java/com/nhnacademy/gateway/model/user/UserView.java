package com.nhnacademy.gateway.model.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserView {
    String userId;
    String email;
    Status status;
}
