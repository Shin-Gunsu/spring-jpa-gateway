package com.nhnacademy.gateway.model;

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
