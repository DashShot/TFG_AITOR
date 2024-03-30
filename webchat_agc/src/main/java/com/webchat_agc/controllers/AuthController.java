package com.webchat_agc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.webchat_agc.dto.User;
import com.webchat_agc.dto.messageModels.AuthMessage;
import com.webchat_agc.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final SimpMessagingTemplate messageTemplate;

    @Autowired
    private final UserService userService;

    
    @MessageMapping("/auth/login")
    public void login(@Payload AuthMessage auth ) {
        System.out.println("PRUEBA-------------------");
        System.out.println(auth.getUsername());

        String response = "";
        User UserAux = this.userService.getByUsername(auth.getUsername());
        System.out.println("PRUEBA-------------------2");
        if (UserAux == null){
            // Handle the login message here
            response = "User not found";
            this.messageTemplate.convertAndSend("/topic/auth/response", response);
            
        }else{
            response = "User found";
            this.messageTemplate.convertAndSend("/topic/auth/response", response);
        }
        
        System.out.println("Message sent to /topic/auth/response");

    }
}
