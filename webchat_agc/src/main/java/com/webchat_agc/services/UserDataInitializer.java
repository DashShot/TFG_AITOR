package com.webchat_agc.services;

import java.io.IOException;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.webchat_agc.dto.ChatRoom;
import com.webchat_agc.dto.ChatRoomStatus;
import com.webchat_agc.dto.User;
import com.webchat_agc.repositories.ChatRoomRepository;
import com.webchat_agc.repositories.UserRepository;

import jakarta.annotation.PostConstruct;



@Service
public class UserDataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private ChatRoomRepository roomRepository;

    @PostConstruct
    public void init() throws IOException, URISyntaxException{
        User user1 = new User("user1", "pass1");
        userRepository.save(user1);

        // Add more users here using the same pattern
        User user2 = new User("user2", "pass2");
        userRepository.save(user2);

        ChatRoom chat1 = new ChatRoom("room1", ChatRoomStatus.AVAILABLE);
        roomRepository.save(chat1);

        ChatRoom chat2 = new ChatRoom("room2", ChatRoomStatus.AVAILABLE);
        roomRepository.save(chat2);
    }

}