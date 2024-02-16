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
    
    private final ChatRoomRepository chatRoomRepository;

    public void saveChatRoom(ChatRoom chatRoom){
        chatRoom.setChatRoomStatus(ChatRoomStatus.AVAILABLE);
        chatRoomRepository.save(chatRoom);
    }
    
    public void closeChatRoom(ChatRoom chatRoom){
        var storedChatRoom = chatRoomRepository.findById(chatRoom.getChatID()).orElse(null);

        if(storedChatRoom != null){
            storedChatRoom.setChatRoomStatus(ChatRoomStatus.UNAVAILABLE);
            chatRoomRepository.save(storedChatRoom);
        }
    }

    public List<ChatRoom> findOpenedChatRooms(){
        return chatRoomRepository.findAllByStatus(ChatRoomStatus.AVAILABLE);

    }
}
