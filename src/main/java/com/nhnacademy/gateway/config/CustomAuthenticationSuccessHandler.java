package com.nhnacademy.gateway.config;

import com.nhnacademy.gateway.model.user.AuthedUser;
import com.nhnacademy.gateway.model.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;



import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    final
    UserService userService;
    final
    PasswordEncoder passwordEncoder;
    final
    RedisTemplate<String, Object> sessionRedisTemplate;

    public CustomAuthenticationSuccessHandler(UserService userService, PasswordEncoder passwordEncoder, RedisTemplate<String, Object> sessionRedisTemplate) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.sessionRedisTemplate = sessionRedisTemplate;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        if(request.getSession(false)!=null) request.getSession().invalidate();

        if(request.getCookies()!=null) {
            for(Cookie c: request.getCookies()) {
                if("SESSIONID".equals(c.getName())) {
                    sessionRedisTemplate.delete("session:" + c.getValue());
                }
            }
        }
        System.out.println("authentication : "  + authentication.getClass());
        String sessionId = UUID.randomUUID().toString();

        AuthedUser authedUser = (AuthedUser)authentication.getPrincipal();

        Cookie sessionCookie = new Cookie("SESSIONID", sessionId);
        System.out.println("sessionId: " + sessionId);
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(60 * 60);
        sessionCookie.setPath("/");
        response.addCookie(sessionCookie);
        sessionRedisTemplate.opsForValue().set(sessionId, authedUser.getUser());

        super.onAuthenticationSuccess(request, response, authentication);
    }
}