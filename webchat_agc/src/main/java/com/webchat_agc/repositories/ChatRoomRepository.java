package com.webchat_agc.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.webchat_agc.dto.ChatRoom;

public interface ChatRoomRepository extends MongoRepository<ChatRoom, String>{
    
    
}
