package com.webchat_agc.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

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
public class Room {
    
    @Id
    private String id;

    private String roomName;
    private ChatRoomStatus chatRoomStatus;


    @DocumentReference
    private List<ChatMessage> chatMessages;
    
    public Room(String roomName, ChatRoomStatus status){
        // this.id = generateRandomId();
        this.roomName  = roomName;
        this.chatRoomStatus = status;
        chatMessages = new ArrayList<>();
    }

    private String generateRandomId() {
        return UUID.randomUUID().toString();
    }

    public void addChatMessage(ChatMessage msg){
        this.chatMessages.add(msg);
        msg.setRoom(this);
    }

}
