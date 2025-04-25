package com.nhnacademy.gateway.model.user;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateCommand {
    private String id;
    private String password;
    private String email;
}
