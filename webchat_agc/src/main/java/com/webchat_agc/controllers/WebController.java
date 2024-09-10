package com.webchat_agc.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.webchat_agc.dto.Room;
import com.webchat_agc.services.RoomService;


import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class WebController {

    @Autowired
    private final RoomService roomService;
    
    @GetMapping("/listRooms")
    public ResponseEntity<List<String>> getlistRooms() {
        List<String> roomsList = new ArrayList<>();
        for  (Room room : this.roomService.getAll()){
                roomsList.add(""+room.getRoomName()+": "+room.getChatRoomStatus());
        }

        System.out.println(roomsList);

        return  ResponseEntity.ok(roomsList);
    }

    // @MessageMapping("/sendMessage")
    // public void sendMessage(@Payload ChatMessage message) throws Exception {
    //     System.out.println("Received: " + message);
    //     ChatMessage  aux = new ChatMessage();
        

}

