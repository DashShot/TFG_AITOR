package com.webchat_agc.controllers;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.webchat_agc.dto.ChatMessage;
import com.webchat_agc.dto.ChatRoom;

@Controller
public class WebController {
    
    @MessageMapping("/login")
    @SendTo("/topic/response")
    public String login(String message) {
        // Handle the login message here
        // Return a response message to be sent to the client
        return new String("Login successful");
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

