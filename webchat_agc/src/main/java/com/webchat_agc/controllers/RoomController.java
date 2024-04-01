package com.webchat_agc.controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

import com.webchat_agc.dto.ChatMessage;
import com.webchat_agc.dto.Room;
import com.webchat_agc.dto.User;
import com.webchat_agc.dto.messageModels.ChatMessageFront;
import com.webchat_agc.services.ChatMessageService;

import com.webchat_agc.services.RoomService;
import com.webchat_agc.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RoomController {
    
    private final SimpMessagingTemplate messageTemplate;

    @Autowired
    private final RoomService roomService;
    
    @Autowired
    private final ChatMessageService messageService;

    @Autowired
    private final UserService userService;


    @MessageMapping("/{roomId}")
    public void roomMessageHandler(@DestinationVariable String roomId, @Payload ChatMessageFront chatMessageFront) {
        System.out.println("MENSAJE RECIBIDO EN LA ROOM: " + roomId);
        System.out.println(chatMessageFront.getUsername() + "En la " + chatMessageFront.getRoom());
        
        //GUARDAR EN BDD

        this.messageTemplate.convertAndSend("/topic/"+roomId,chatMessageFront);
    }

    @MessageMapping("/sendMessage")
    public void sendMessage(@Payload ChatMessageFront chatMessageFront){
        System.out.println(chatMessageFront.getContent()+" --- Prueba --- "+chatMessageFront.toString());
        String response = "";
        Room room = roomService.getByName(chatMessageFront.getRoom());
        User user = userService.getByUsername(chatMessageFront.getUsername());
        
        ChatMessage chatMessage = new ChatMessage(chatMessageFront.getContent(), new Date(), user, room );
        System.out.print(chatMessage.getContent());

        room.addChatMessage(chatMessage);

        messageService.saveChatMessage(chatMessage);
        roomService.saveRoom(room);
        response = "Mensaje creado y enviado correctamente";
        this.messageTemplate.convertAndSend("/topic/sendMessage",response);
        System.out.println("Message sent to /topic/sendMessages");
    }

    @MessageMapping("/getMessages")
    public void getRoomMessages(String roomName) throws Exception {
        List<String> messagesRoomList = new ArrayList<>();
        for (ChatMessage msg : this.roomService.getByName(roomName).getChatMessages()) {
            messagesRoomList.add("From: " + msg.getSender().getUsername() + " Content: " + msg.getContent() + " Room: " + msg.getRoom().getRoomName() + " Date: " + msg.getTimestamp());
        }

        System.out.println(messagesRoomList);

        this.messageTemplate.convertAndSend("/topic/getMessages",messagesRoomList);
        System.out.println("Message sent to /topic/getMessages");
    }

    
}
