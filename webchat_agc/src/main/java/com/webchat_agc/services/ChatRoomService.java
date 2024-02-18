package com.webchat_agc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

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

    public void closeChatRoom(ChatRoom chatRoom){
        var storedChatRoom = repository.findById(chatRoom.getChatID()).orElse(null);

        if(storedChatRoom != null){
            storedChatRoom.setChatRoomStatus(ChatRoomStatus.UNAVAILABLE);
            repository.save(storedChatRoom);
        }
    }

    @GetMapping("/rooms")
    public List<ChatRoom> findAvailableChatRooms(){
        return repository.findAllByStatus(ChatRoomStatus.AVAILABLE);

    }

    public Optional<ChatRoom> findById(String chatRoomID) {
        return repository.findById(chatRoomID);
    }
    
}
