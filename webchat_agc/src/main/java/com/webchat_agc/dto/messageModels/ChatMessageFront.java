package com.webchat_agc.dto.messageModels;

import java.util.Date;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatMessageFront {
    
    private String content;
    private String username;
    private String room;

    public ChatMessageFront(String content, Date timestamp, String username, String room){
        this.content = content;
        this.username = username;
        this.room = room;
    }
    
}
