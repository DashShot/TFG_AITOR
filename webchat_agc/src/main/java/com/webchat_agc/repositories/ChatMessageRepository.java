package com.webchat_agc.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.webchat_agc.dto.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, Long> {
    
   // public List<ChatMessage> findByChatId(String id);
}
