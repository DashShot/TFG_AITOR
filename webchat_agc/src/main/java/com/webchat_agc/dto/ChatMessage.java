package com.webchat_agc.dto;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Document("Mensajes")
public class ChatMessage {
    
    @Id
    private String id;

    private String content;
    private Date timestamp;
    
    //AÑADIR DEPENDENCIAS
    private String senderId;
    private String roomId;    

    public ChatMessage(String content, Date timestamp, String senderId, String roomId){
        this.content = content;
        this.timestamp= timestamp;
        this.senderId = senderId;
        this.roomId = roomId;        
    }
}
