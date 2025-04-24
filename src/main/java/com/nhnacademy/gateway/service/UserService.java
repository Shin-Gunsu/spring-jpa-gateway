package com.nhnacademy.gateway.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.nhnacademy.gateway.model.User;
import com.nhnacademy.gateway.model.UserCreateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    final
    RestTemplate restTemplate;



    public UserService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public User createUser(UserCreateCommand userCreateCommand) {
        return restTemplate.postForObject("http://localhost:8080/api/users/", userCreateCommand, User.class);
    }

    public User getUser(String id) {
        return restTemplate.getForObject("http://localhost:8080/api/users/"+id, User.class);
    }
}
