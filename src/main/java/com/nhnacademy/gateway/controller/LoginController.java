package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.model.user.UserLoginRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {
    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping("/login/process")
    public String process(@RequestBody UserLoginRequest userLoginRequest) {
        return "redirect:/";
    }
}
