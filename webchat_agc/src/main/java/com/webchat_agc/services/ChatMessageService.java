package com.webchat_agc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webchat_agc.dto.ChatMessage;
import com.webchat_agc.dto.Room;
import com.webchat_agc.repositories.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    
    @Autowired
    private final ChatMessageRepository repository;

    public void saveChatMessage(ChatMessage chatMessage){
         repository.save(chatMessage);
    }

    // //REVISAR PARA CHAT ROOM NO ENCONTRADOS
    // public List<ChatMessage> findChatMessagesByID(String chatRoomId){
    //     return repository.findByChatId(chatRoomId);
    // }
    

}
