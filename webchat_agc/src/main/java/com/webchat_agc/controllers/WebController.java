package com.webchat_agc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.webchat_agc.dto.AuthMessage;
import com.webchat_agc.dto.ChatMessage;
import com.webchat_agc.dto.ChatRoom;
import com.webchat_agc.dto.User;
import com.webchat_agc.services.ChatMessageService;
import com.webchat_agc.services.ChatRoomService;
import com.webchat_agc.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebController {
    
    private final SimpMessagingTemplate messageTemplate;

    @Autowired
    private final UserService userService;
    
    @Autowired
    private final ChatRoomService roomService;

    
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
        
    @MessageMapping("/listRooms")
    public void  listRooms() throws Exception {
        List<String> roomsList = new ArrayList<>();
        for  (ChatRoom room : this.roomService.getAll()){
                roomsList.add(""+room.getChatID()+": "+room.getChatRoomStatus());
        }
        System.out.println(roomsList);

        this.messageTemplate.convertAndSend("/topic/listRooms",roomsList);
        System.out.println("Message sent to /topic/listRooms");
    }
}

