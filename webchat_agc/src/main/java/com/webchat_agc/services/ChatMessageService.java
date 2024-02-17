package com.webchat_agc.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.webchat_agc.dto.ChatMessage;
import com.webchat_agc.dto.ChatRoom;
import com.webchat_agc.repositories.ChatMessageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    
    private final ChatMessageRepository repository;


    public void save(ChatMessage chatMessage, ChatRoom chatRoom){

        chatMessage.setChatRoomId(chatRoom.getChatID());
        chatMessage.setReceivers(chatRoom.getRoomUsers()); //Revisar quitar sender

        repository.save(chatMessage);
    }

    public List<ChatMessage> findChatMessages(ChatRoom chatRoom){

        return repository.findByChatId(chatRoom.getChatID());
    }
}
