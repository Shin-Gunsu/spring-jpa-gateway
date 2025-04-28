package com.nhnacademy.gateway.model.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.nhnacademy.gateway.model.user.UserCreateCommand;
import com.nhnacademy.gateway.model.user.UserLoginRequest;
import com.nhnacademy.gateway.model.user.UserView;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    final
    RestTemplate restTemplate;

    final
    ObjectMapper redisObjectMapper;


    private final PasswordEncoder passwordEncoder;

    public UserService(ObjectMapper redisObjectMapper, RedisTemplate<String, Object> redisTemplate, PasswordEncoder passwordEncoder, RestTemplate restTemplate) {
        this.redisObjectMapper = redisObjectMapper;
        this.passwordEncoder = passwordEncoder;
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<UserView> getUser(String url, HttpEntity<UserLoginRequest> req) {
        return restTemplate.postForEntity(url, req, UserView.class);
    }

    public ResponseEntity<UserView> createUser(UserCreateCommand cmd) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UserCreateCommand> request = new HttpEntity<>(cmd, headers);

        String url = "http://localhost:8081/api/users";
        return restTemplate.postForEntity(url, request, UserView.class);
    }

}
