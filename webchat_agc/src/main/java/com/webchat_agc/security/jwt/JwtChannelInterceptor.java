package com.webchat_agc.security.jwt;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtChannelInterceptor implements ChannelInterceptor {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        System.out.println("--Channel Interceptor--");
        System.out.println(message);

          // Verificar si el mensaje es del tipo CONNECT
        if (accessor.getCommand() == StompCommand.CONNECT) {
            // Buscar el token JWT en los headers nativos
            String authHeader = accessor.getFirstNativeHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwtToken = authHeader.substring(7);  // Eliminar "Bearer "
                System.out.println("JWT detectado: " + jwtToken);

                String decryptToken = SecurityCipher.decrypt(jwtToken);
                System.out.println("JWT Decrypt: " + decryptToken);
                
                if (jwtTokenProvider.validateToken(decryptToken)) {
                    String username = jwtTokenProvider.getUsername(decryptToken);
                    System.out.println("Usuario autenticado: " + username);
                    
                   // Establecer la autenticación en el contexto de seguridad
                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("Token inválido");
                }
            } else {
                System.out.println("No se encontró token JWT en los headers.");
            }
        }
         return message;
     }
}

 