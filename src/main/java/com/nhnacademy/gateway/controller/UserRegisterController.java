package com.nhnacademy.gateway.controller;


import com.nhnacademy.gateway.model.user.UserCreateCommand;
import com.nhnacademy.gateway.model.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
public class UserRegisterController {
    final
    UserService userService;
    final
    RestTemplate restTemplate;

    public UserRegisterController(UserService userService, RestTemplate restTemplate) {
        this.userService = userService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/user/register")
    public String showForm() {
        return "userRegister";
    }

    @ResponseBody
    @PostMapping(value = "/user/register")
    public ResponseEntity<?>  register(@ModelAttribute UserCreateCommand cmd) {
        System.out.println("register called");
        System.out.println(cmd.getUserId());
        System.out.println(cmd.getPassword());
        System.out.println(cmd.getEmail());
        System.out.println(userService.createUser(cmd));
        return ResponseEntity.ok().build();
    }
}
