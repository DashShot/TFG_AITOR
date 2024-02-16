package com.webchat_agc.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.webchat_agc.dto.User;
import com.webchat_agc.services.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @MessageMapping("/user.addUser")
    @SendTo("/user/room")
    public User addUser(@Payload User user){
        service.saveUser(user);
        return user;
    }

    @MessageMapping("/user.disconnectUser")
    @SendTo("/user/room")
    public User disconnect(@Payload User user){
        service.disconnect(user);
        return user;
    }
    
    //IGUAL PARA ROOMS

    @GetMapping("/Users")
    public ResponseEntity<List<User>> findConnectedUsers(){
        return ResponseEntity.ok(service.findConnectedUsers());
    }
    
}
