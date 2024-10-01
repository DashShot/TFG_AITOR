package com.webchat_agc.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.webchat_agc.dto.ChatRoomStatus;
import com.webchat_agc.dto.Room;
import com.webchat_agc.services.RoomService;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequiredArgsConstructor
public class WebController {

    @Autowired
    private final RoomService roomService;
    
    @GetMapping("/api/listRooms")
    public ResponseEntity<List<String>> getlistRooms() {
        List<String> roomsList = new ArrayList<>();
        for  (Room room : this.roomService.getAll()){
                roomsList.add(""+room.getRoomName()+": "+room.getChatRoomStatus());
        }

        System.out.println(roomsList);

        return  ResponseEntity.ok(roomsList);
    }
    
   @PostMapping("/api/createRoom")
    public ResponseEntity<Map<String, String>> createRoom(@RequestBody String roomId) {
        Map<String, String> response = new HashMap<>();
        // Verificar si la sala ya existe
        if (roomService.existsByRoomName(roomId)) {
            response.put("message", "Room already exists: " + roomId);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Si no existe, crear una nueva sala
        Room newRoom = new Room(roomId, ChatRoomStatus.AVAILABLE);
        roomService.saveRoom(newRoom);

        response.put("message", "Room created successfully: " + roomId);
        System.out.println(response);

        return ResponseEntity.ok(response);
    }
    
}

