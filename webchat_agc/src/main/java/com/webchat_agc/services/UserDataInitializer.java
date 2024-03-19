package com.webchat_agc.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webchat_agc.dto.User;
import com.webchat_agc.repositories.UserRepository;

import jakarta.annotation.PostConstruct;



@Service
public class UserDataInitializer {

    @Autowired
    private UserRepository userRepository;

    @PostConstruct
    public void init() throws IOException, URISyntaxException{
        User user1 = new User("user1", "pass1");
        userRepository.save(user1);

        // Add more users here using the same pattern
        User user2 = new User("user2", "pass2");
        userRepository.save(user2);
    }

}