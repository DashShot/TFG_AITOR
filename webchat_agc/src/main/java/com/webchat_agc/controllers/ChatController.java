package com.webchat_agc.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.webchat_agc.dto.ChatMessage;
import com.webchat_agc.dto.ChatNotification;
import com.webchat_agc.dto.ChatRoom;
import com.webchat_agc.services.ChatMessageService;
import com.webchat_agc.services.ChatRoomService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class ChatController {
    
    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    private final SimpMessagingTemplate messagingTemplate;


    @GetMapping("/messages/{roomID}")
    public ResponseEntity<List<ChatMessage>> findChatMessages(@PathVariable("roomID") String roomID){
        return ResponseEntity.ok(chatMessageService.findChatMessagesByID(roomID));
    }

    //Mejorar Soprte opcional
    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage chatMessage, @Payload String chatRoomID){

        ChatRoom chatRoom = chatRoomService.findById(chatRoomID).orElse(null);
        if(chatRoom == null){
            //LANZAR EXCEP mensje No procesado

        }else{
            chatMessageService.save(chatMessage, chatRoom);
            //  /user/queue/messages
            messagingTemplate.convertAndSendToUser(chatMessage.getSenderId()
                                                    ,"/queue/messages"
                                                    ,ChatNotification.builder().id(chatMessage.getId())
                                                                                .senderId(chatMessage.getSenderId())
                                                                                .roomId(chatRoomID)
                                                                                .content(chatMessage.getContent()));

        }
    }

}
