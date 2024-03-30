package com.webchat_agc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


import com.webchat_agc.dto.Room;
import com.webchat_agc.services.RoomService;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebController {
    
    private final SimpMessagingTemplate messageTemplate;

    @Autowired
    private final RoomService roomService;

    
    @MessageMapping("/listRooms")
    public void  listRooms() throws Exception {
        List<String> roomsList = new ArrayList<>();
        for  (Room room : this.roomService.getAll()){
                roomsList.add(""+room.getRoomName()+": "+room.getChatRoomStatus());
        }
        System.out.println(roomsList);

        this.messageTemplate.convertAndSend("/topic/listRooms",roomsList);
        System.out.println("Message sent to /topic/listRooms");
    }

    // @MessageMapping("/sendMessage")
    // public void sendMessage(@Payload ChatMessage message) throws Exception {
    //     System.out.println("Received: " + message);
    //     ChatMessage  aux = new ChatMessage();
        

}

