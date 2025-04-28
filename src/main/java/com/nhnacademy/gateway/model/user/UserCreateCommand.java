package com.nhnacademy.gateway.model.user;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.validation.constraints.Email;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateCommand {
    private String userId;
    private String password;
    @Email
    private String email;
    private Status status;
}
