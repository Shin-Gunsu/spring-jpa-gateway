package com.nhnacademy.gateway.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.List;


@Getter
@AllArgsConstructor
public class AuthedUser implements CustomUserDetails {
    final User user;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String status = "Status_" + user.getStatus();
        return List.of(new SimpleGrantedAuthority(status));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }


    @Override
    public String getUsername() {
        return user.getId();
    }

    @Override
    public String getEmail() {
        return user.getEmail();
    }
}
