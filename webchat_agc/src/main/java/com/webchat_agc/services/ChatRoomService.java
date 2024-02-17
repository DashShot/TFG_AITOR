package com.webchat_agc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.webchat_agc.dto.ChatRoom;
import com.webchat_agc.dto.ChatRoomStatus;
import com.webchat_agc.repositories.ChatRoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    
    private final ChatRoomRepository repository;

    public void saveChatRoom(ChatRoom chatRoom){
        chatRoom.setChatRoomStatus(ChatRoomStatus.AVAILABLE);
        repository.save(chatRoom);
    }

    ////////////////////

    //private void createChatRoom() ?

    ///

    public void closeChatRoom(ChatRoom chatRoom){
        var storedChatRoom = repository.findById(chatRoom.getChatID()).orElse(null);

        if(storedChatRoom != null){
            storedChatRoom.setChatRoomStatus(ChatRoomStatus.UNAVAILABLE);
            repository.save(storedChatRoom);
        }
    }

    public List<ChatRoom> findOpenedChatRooms(){
        return repository.findAllByStatus(ChatRoomStatus.AVAILABLE);

    }


    ///////////////////////////////////////////////////////


    
}
