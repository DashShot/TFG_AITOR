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
        //chatRoom.setChatRoomStatus(ChatRoomStatus.AVAILABLE);
        repository.save(chatRoom);
    }

    // public void closeChatRoom(ChatRoom chatRoom){
    //     var storedChatRoom = repository.findById(chatRoom.getChatID()).orElse(null);

    //     if(storedChatRoom != null){
    //         storedChatRoom.setChatRoomStatus(ChatRoomStatus.UNAVAILABLE);
    //         repository.save(storedChatRoom);
    //     }
    // }

    
    public Optional<Room> findById(String chatRoomID) {
        return repository.findById(chatRoomID);
    }
    
    public  List<Room> getAll(){
        return repository.findAll();
    }
    public Room getByName(String roomName){
        return repository.findByRoomName(roomName);
    }
}
