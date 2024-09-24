package com.webchat_agc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.webchat_agc.dto.ChatMessage;
import com.webchat_agc.dto.Room;
import com.webchat_agc.dto.ChatRoomStatus;
import com.webchat_agc.dto.User;
import com.webchat_agc.repositories.ChatMessageRepository;
import com.webchat_agc.repositories.RoomsRepository;
import com.webchat_agc.repositories.UserRepository;

import jakarta.annotation.PostConstruct;

@Service
public class UserDataInitializer {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoomsRepository roomRepository;

    @Autowired
    private ChatMessageRepository messageRepository;

    @PostConstruct
    public void init() {
        try {
            // BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

            // User user1 = new User("user1", passwordEncoder.encode("pass1"),"USER");
            // userRepository.save(user1);

            // User user2 = new User("user2", passwordEncoder.encode("pass2"), "USER");
            // userRepository.save(user2);

            // Room chat1 = new Room("room1", ChatRoomStatus.AVAILABLE);
            // roomRepository.save(chat1);
            // Room chat2 = new Room("room2", ChatRoomStatus.AVAILABLE);
            // roomRepository.save(chat2);

            // ChatMessage msg1 = new ChatMessage("msg prueba 1", new Date(), user1.getId(), chat1.getId());
            // messageRepository.save(msg1);

            // ChatMessage msg2 = new ChatMessage("msg prueba 2", new Date(), user2.getId(), chat1.getId());
            // messageRepository.save(msg2);

            // ChatMessage msg3 = new ChatMessage("prueba3", new Date() , user1.getId(), chat2.getId());
            // messageRepository.save(msg3);

            // ChatMessage msg4 = new ChatMessage("prueba4", new Date() , user2.getId(), chat2.getId());
            // messageRepository.save(msg4);


        } catch (DataAccessException e) {
            // Handle data access exceptions appropriately (e.g., log the error)
            e.printStackTrace();
        }
    }
}