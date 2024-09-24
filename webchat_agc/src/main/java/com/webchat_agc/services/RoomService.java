package com.webchat_agc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webchat_agc.dto.Room;
import com.webchat_agc.repositories.RoomsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
    
    @Autowired
    private final RoomsRepository repository;

    public void saveRoom(Room chatRoom){
        repository.save(chatRoom);
    }

    public Optional<Room> getById(String roomId) {
        return repository.findById(roomId);
    }
    
    public  List<Room> getAll(){
        return repository.findAll();
    }
   
    // Nuevo método para obtener el ID de la sala por su nombre
    public Optional<String> getRoomIdByRoomName(String roomName) {
        return repository.findByRoomName(roomName)  // Busca la sala por nombre
                         .map(Room::getId);  // Obtén el ID si la sala existe
    }
    
    public boolean existsByRoomName(String roomName) {
        return repository.existsByRoomName(roomName);
    }

}
