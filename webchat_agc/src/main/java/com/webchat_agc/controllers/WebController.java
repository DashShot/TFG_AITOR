package com.webchat_agc.controllers;

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
import com.webchat_agc.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebController {
    
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

        // Return a response message to the client
        //this.messaging.convertAndSend("/topic/response", response);
    }
        

    @MessageMapping("/register")
    @SendTo("/topic/response")
    public String register(String message) {
        // Handle the registration message here
        // Return a response message to be sent to the client
        return new String("Registration successful");
    }

    @MessageMapping("/disconnect")
    @SendTo("/topic/response")
    public String disconnect(String message) {
        // Handle the registration message here
        // Return a response message to be sent to the client
        return new String("Disconnected");
    }

    @MessageMapping("/rooms")
    @SendTo("/topic/rooms")
    public String roomsDisplay(){
        //return new ChatMessage(chatMessage);
        return new String("1");
    }


    @MessageMapping("/chat/{chatRoomId}")
    @SendTo("/topic/{chatRoomId}")
    public ChatMessage chatMessage(@DestinationVariable String chaRoomID, ChatMessage chatMessage, ChatRoom chatRoom){
        //return new ChatMessage(chatMessage);
        return chatMessage;
    }

    // @GetMapping("/chat/{roomID}")
    // public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable("roomID") String roomID){
    //     return ResponseEntity.ok(chatMessageService.findChatMessagesByID(roomID));
    // }

    // //Mejorar Soprte opcional
    // @MessageMapping("/chat")
    // public void processMessage(@Payload ChatMessage chatMessage, @Payload String chatRoomID){

    //     ChatRoom chatRoom = chatRoomService.findById(chatRoomID).orElse(null);
    //     if(chatRoom == null){
    //         //LANZAR EXCEP mensje No procesado

    //     }else{
    //         chatMessageService.save(chatMessage, chatRoom);
    //         //  /user/queue/messages
    //         messagingTemplate.convertAndSendToUser(chatMessage.getSenderId()
    //                                                 ,"/queue/messages"
    //                                                 ,ChatNotification.builder().id(chatMessage.getId())
    //                                                                             .senderId(chatMessage.getSenderId())
    //                                                                             .roomId(chatRoomID)
    //                                                                             .content(chatMessage.getContent()));

    //     }
    // }


}

