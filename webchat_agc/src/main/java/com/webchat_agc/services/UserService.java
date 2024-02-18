package com.webchat_agc.services;

import java.util.List;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
import com.webchat_agc.dto.User;
import com.webchat_agc.dto.UserStatus;
import com.webchat_agc.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @MessageMapping("/user.addUser")
    @SendTo("/user/topic")
    public void saveUser(User user){
        user.setStatus(UserStatus.ONLINE);
        repository.save(user);
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/topic")
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
