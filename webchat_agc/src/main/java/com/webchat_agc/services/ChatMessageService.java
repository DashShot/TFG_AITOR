package com.webchat_agc.services;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.webchat_agc.dto.ChatMessage;
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

    // Obtener los últimos mensajes de una sala
    public List<ChatMessage> getLastMessages(String roomId, int numMsg) {
        Pageable pageable = PageRequest.of(0, numMsg, Sort.by("timestamp").descending());
        List<ChatMessage>  messages = repository.findByRoomOrderByTimestampDesc(roomId, pageable);

        Collections.reverse(messages);
        return messages;
    }
    
}
