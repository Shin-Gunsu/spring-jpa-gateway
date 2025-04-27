package com.nhnacademy.gateway.model.user.service;

import com.nhnacademy.gateway.model.user.User;
import com.nhnacademy.gateway.model.user.UserCreateCommand;
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