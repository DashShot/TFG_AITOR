package com.webchat.webchat_agc.config;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.webchat.webchat_agc.chat.ChatMessage;
import com.webchat.webchat_agc.chat.MessageType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    
    private final SimpMessageSendingOperations messageTemplate;

    @EventListener
    public void HandleWebSocketDisconnectListener(SessionDisconnectEvent event){

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if (username != null){
            log.info("User disconnected: ",username);
            var chatMessage = ChatMessage.builder().messageType(MessageType.LEAVE).sender(username).build();

            messageTemplate.convertAndSend("/topic/public", chatMessage);

        }
    }
}
