package com.nhnacademy.gateway.filter;

import com.nhnacademy.gateway.model.AuthedUser;
import com.nhnacademy.gateway.model.User;
import com.nhnacademy.gateway.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private final PasswordEncoder passwordEncoder;

    private final RedisTemplate<String, Object> sessionRedisTemplate;

    private final UserService userService;
    public UserAuthenticationFilter(RedisTemplate<String, Object> sessionRedisTemplate,PasswordEncoder passwordEncoder, UserService userService) {
        this.sessionRedisTemplate = sessionRedisTemplate;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //TODO
        String sessionId = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                }
            }
        }

        if (sessionId != null) {
            Object o = sessionRedisTemplate.opsForValue().get(sessionId);
            String id = (String) o;
            System.out.println("id: " + id);
            if (id != null) {
                User user = userService.getUser(id);
                AuthedUser authedUser = new AuthedUser(user, passwordEncoder);
                Authentication auth = new PreAuthenticatedAuthenticationToken(authedUser, null, authedUser.getAuthorities());
                auth.setAuthenticated(true);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }


        filterChain.doFilter(request, response);
    }

}