package com.webchat_agc.dto;

import java.util.Date;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document
public class ChatMessage {
    
    @Id
    private String id;

    private String content;
    private Date timestamp;
    
    //AÑADIR DEPENDENCIAS
    @DocumentReference
    private User sender;

    @DocumentReference
    private Room room;    

    public ChatMessage(String content, Date timestamp, User sender, Room room){
        this.id = generateRandomId();
        this.content = content;
        this.timestamp= timestamp;
        this.sender = sender;
        this.room = room;        
    }
    
    private String generateRandomId() {
        return UUID.randomUUID().toString();
    }

}
