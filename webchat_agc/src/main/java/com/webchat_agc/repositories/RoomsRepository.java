package com.webchat_agc.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.webchat_agc.dto.Room;


public interface RoomsRepository extends MongoRepository<Room, String>{

  // List<Room> findAllByChatRoomStatus(ChatRoomStatus AVAILABLE);
   
   Optional<Room> findByRoomName(String roomName);  // Método que busca por el nombre de la sala

   boolean existsByRoomName(String roomName);
}
