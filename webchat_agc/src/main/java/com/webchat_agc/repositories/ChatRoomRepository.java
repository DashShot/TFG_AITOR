package com.webchat_agc.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.webchat_agc.dto.ChatRoom;
import com.webchat_agc.dto.ChatRoomStatus;


public interface ChatRoomRepository extends MongoRepository<ChatRoom, String>{

   List<ChatRoom> findAllByChatRoomStatus(ChatRoomStatus AVAILABLE);
    
    
}
