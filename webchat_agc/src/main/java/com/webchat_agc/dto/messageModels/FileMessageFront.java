package com.webchat_agc.dto.messageModels;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class FileMessageFront {

    private String room;
    private String username;
    private String filename;
    private String fileType;
    private String content;

    public FileMessageFront(String room, String username, String filename, String fileType, String content){
        this.room = room;
        this.username = username;
        this.filename = filename;
        this.fileType = fileType;
        this.content = content;
    }
    
}