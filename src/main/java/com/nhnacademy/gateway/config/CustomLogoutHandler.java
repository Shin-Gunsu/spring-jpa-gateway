package com.nhnacademy.gateway.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

@Component
public class CustomLogoutHandler implements LogoutSuccessHandler {

    RedisTemplate<String, Object> redisTemplate;

    public CustomLogoutHandler(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();
        String sid = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSIONID".equals(cookie.getName())) {
                    sid = cookie.getValue();
                    break;
                }
            }
        }

        if (sid != null) {
            redisTemplate.delete("session:" + sid);
        }

        Cookie expired = new Cookie("SESSIONID", "");
        expired.setPath("/");
        expired.setMaxAge(0);
        response.addCookie(expired);

        response.sendRedirect("/");
    }
}
