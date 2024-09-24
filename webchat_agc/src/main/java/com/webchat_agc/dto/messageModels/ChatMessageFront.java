package com.webchat_agc.dto.messageModels;

import java.util.Date;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ChatMessageFront {
    
    private String room;
    private String username;
    private String content;
    private Date timeStamp;

    public ChatMessageFront( String room, String username, String content, Date timeStamp){
        this.room = room;
        this.username = username;
        this.content = content;
        this.timeStamp = timeStamp;
    }
    
}
