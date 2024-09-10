package com.webchat_agc.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webchat_agc.dto.User;
import com.webchat_agc.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository repository;
    
    public Optional<User> getByUsername(String username) {
            return repository.findByUsername(username);
    }

    public void saveUser(User user){
        repository.save(user);
    }
}
