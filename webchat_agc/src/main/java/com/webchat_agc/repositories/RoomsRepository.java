package com.webchat_agc.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.webchat_agc.dto.Room;
import com.webchat_agc.dto.ChatRoomStatus;


public interface RoomsRepository extends MongoRepository<Room, String>{

   List<Room> findAllByChatRoomStatus(ChatRoomStatus AVAILABLE);
   Room findByRoomName(String roomName);
    
}
