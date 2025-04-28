package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.model.CustomUserDetails;
import com.nhnacademy.gateway.model.user.AuthedUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView home(@AuthenticationPrincipal AuthedUser authedUser) {
        ModelAndView home = new ModelAndView("home");
        if(authedUser != null) {
            home.addObject("user", authedUser);
        }

        return home;
    }

}
