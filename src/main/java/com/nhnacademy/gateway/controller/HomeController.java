package com.nhnacademy.gateway.controller;

import com.nhnacademy.gateway.model.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/main")
    public ModelAndView home(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        ModelAndView home = new ModelAndView("user");
        home.addObject("userName", customUserDetails.getUsername());
        home.addObject("userEmail", customUserDetails.getEmail());
        return home;
    }

}
