package com.webchat_agc.services;

//import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.messaging.handler.annotation.MessageMapping;
//import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.GetMapping;

import com.webchat_agc.dto.User;
//import com.webchat_agc.dto.UserStatus;
import com.webchat_agc.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository repository;
    
    public User getByUsername(String username) {
            return repository.getByUsername(username);
    }

    public void saveUser(User user){
        repository.save(user);
    }

    // @MessageMapping("/user.addUser")
    // @SendTo("/user/topic")
    // public void saveUser(User user){
    //     user.setStatus(UserStatus.ONLINE);
    //     repository.save(user);
    // }

    // @MessageMapping("/user.disconnectUser")
    // @SendTo("/user/topic")
    // public void disconnect(User user){
    //     var storedUser = repository.findById(user.getId()).orElse(null);
        
    //     if(storedUser != null){
    //         storedUser.setStatus(UserStatus.OFFLINE);
    //         repository.save(storedUser);
    //     }
    // }

    // @GetMapping("/users")
    // public List<User> findConnectedUsers(){
    //     return repository.findAllByStatus(UserStatus.ONLINE);
    // }

    // public User getByNickname(String nickName) {
    //         return repository.getByNickname(nickName);
    // }

    
}
