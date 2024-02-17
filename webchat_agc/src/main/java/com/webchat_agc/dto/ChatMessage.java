package com.webchat_agc.dto;

import java.util.Date;
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
public class ChatMessage {
    
    @Id
    private String id;
    private String chatRoomId;

    private String content;
    private Date timestamp;
    
    //AÑADIR DEPENDENCIAS
    private String senderId;

    private List<User> receivers;

    

}
