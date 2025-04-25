package com.nhnacademy.gateway.service;

import com.nhnacademy.gateway.model.user.AuthedUser;
import com.nhnacademy.gateway.model.CustomUserDetails;
import com.nhnacademy.gateway.model.user.User;
import com.nhnacademy.gateway.model.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public CustomUserDetailsService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            User user = userService.getUser(username);
            return new AuthedUser(user, passwordEncoder);

        } catch (UserNotFoundException e) {
            throw new UsernameNotFoundException("No such user: " + username, e);
        }
    }
}
