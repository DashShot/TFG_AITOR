package com.webchat_agc.services;

import java.util.List;

import org.springframework.stereotype.Service;
import com.webchat_agc.dto.User;
import com.webchat_agc.dto.UserStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public void saveUser(User user){
        user.setStatus(UserStatus.ONLINE);
        repository.save(user);
    }

    public void disconnect(User user){
        var storedUser = repository.findById(user.getNickName()).orElse(null);
        
        if(storedUser != null){
            storedUser.setStatus(UserStatus.OFFLINE);
            repository.save(storedUser);
        }
    }

    public List<User> findConnectedUsers(){
        return repository.findAllByStatus(UserStatus.ONLINE);
    }
    
}
