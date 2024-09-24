package com.webchat_agc.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.webchat_agc.dto.ChatMessage;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
    
   // public List<ChatMessage> findByChatId(String id);

   // Método para obtener los últimos mensajes de una sala, ordenados por timestamp
   @Query("{ 'roomId': ?0 }, sort: { 'timestamp': -1 }")
   List<ChatMessage> findByRoomOrderByTimestampDesc(String roomId, Pageable pageable);

   @Query("{ 'roomId': ?0 }")
   List<ChatMessage> findByRoomOrderByTimestampAsc(String roomId, Pageable pageable);


}
 