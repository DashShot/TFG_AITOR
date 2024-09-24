package com.webchat_agc.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document("Room")
public class Room {
    
    @Id
    private String id;

    @Indexed(unique = true)
    private String roomName;

    private ChatRoomStatus chatRoomStatus;

    public Room(String roomName, ChatRoomStatus status){
        this.roomName  = roomName;
        this.chatRoomStatus = status;
    }

}
