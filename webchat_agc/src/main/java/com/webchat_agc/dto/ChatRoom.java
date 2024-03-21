package com.webchat_agc.dto;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class ChatRoom {
    
    @Id
    private String chatID;

    private ChatRoomStatus chatRoomStatus;

    //AÑADIR DEPENDENCIAS
    private List<User> roomUsers;
    private List<ChatMessage> chatMessages;
    
    public ChatRoom(String chatID, ChatRoomStatus status){
        this.chatID  = chatID;
        this.chatRoomStatus = status;
        roomUsers = null;
        chatMessages = null;
    }
}
