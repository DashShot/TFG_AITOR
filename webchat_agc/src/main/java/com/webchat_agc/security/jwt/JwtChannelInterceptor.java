package com.webchat_agc.security.jwt;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("--Channel Interceptor--");
        System.out.println(message);

        // String jwtToken = accessor.getFirstNativeHeader("Authorization");
        // if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
        //     jwtToken = jwtToken.substring(7);

        //     if (jwtTokenProvider.validateToken(jwtToken)) {
        //         String username = jwtTokenProvider.getUsername(jwtToken);
        //         System.out.println(username + "Detectado");
        //         //  Set the authentication in the SecurityContext if needed
        //     }
        // }
        //String simUser = accessor.getHeader("simpUser");

        //System.out.println("User + " + accessor.getFirstNativeHeader("simpUser"));
        return message;
    }
}
