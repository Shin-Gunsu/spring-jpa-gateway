package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.exception.BlockedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        //TODO
        if(exception instanceof BlockedException) {
            response.sendRedirect("/auth/blocked");
            return;
        }
        String id = request.getParameter("id");
        System.out.println("login fail : " + id);

        response.sendRedirect("/login?error=true");
    }


}