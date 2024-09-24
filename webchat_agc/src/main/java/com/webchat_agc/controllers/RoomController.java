package com.webchat_agc.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import com.webchat_agc.dto.ChatMessage;
import com.webchat_agc.dto.Room;
import com.webchat_agc.dto.User;
import com.webchat_agc.dto.messageModels.ChatMessageFront;
import com.webchat_agc.dto.messageModels.FileMessageFront;
import com.webchat_agc.services.ChatMessageService;
import com.webchat_agc.services.RoomService;
import com.webchat_agc.services.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class RoomController {
    
    private final SimpMessagingTemplate messageTemplate;

    @Autowired
    private final ChatMessageService messageService;

    @Autowired
    private final RoomService roomService;

    @Autowired
    private final UserService userService;


    @MessageMapping("/{roomId}")
    public void roomMessageHandler(@DestinationVariable String roomId, @Payload ChatMessageFront chatMessageFront) {
        // Log de recepción del mensaje
        System.out.println("MENSAJE RECIBIDO EN LA ROOM: " + roomId);
        System.out.println(chatMessageFront.getUsername() + " en la sala " + chatMessageFront.getRoom() + " a la hora: " + chatMessageFront.getTimeStamp());

        try {
             // Verificar si el usuario es "Server"
             if ("Server".equals(chatMessageFront.getUsername())) {
                // Si el usuario es "Server", solo enviar el mensaje al canal sin guardar en la base de datos **POSTERIOR  EDICION  PARA REG LOGS
                this.messageTemplate.convertAndSend("/topic/" + roomId, chatMessageFront);
                return;
            }

            // Buscar el ID de la sala en la base de datos
            Optional<String> roomBDDId = roomService.getRoomIdByRoomName(roomId);

            // Verificar si se encontró el ID de la sala
            if (roomBDDId.isPresent()) {
                // Obtener el ID del usuario a partir del nombre de usuario
                Optional<User> userOptional = userService.getByUsername(chatMessageFront.getUsername());

                if (userOptional.isPresent()) {
                    // Crear un nuevo mensaje de chat
                    ChatMessage msg = new ChatMessage(
                            chatMessageFront.getContent(),
                            chatMessageFront.getTimeStamp(),
                            userOptional.get().getId(),
                            roomBDDId.get()
                    );

                    // Guardar el mensaje en la base de datos
                    messageService.saveChatMessage(msg);
                } else {
                    // Manejo del caso en que el usuario no se encuentra
                    throw new IllegalArgumentException("Usuario no encontrado: " + chatMessageFront.getUsername());
                }
            } else {
                // Manejo del caso en que la sala no se encuentra
                throw new IllegalArgumentException("Sala no encontrada: " + roomId);
            }

            // Enviar el mensaje al canal correspondiente
            messageTemplate.convertAndSend("/topic/" + roomId, chatMessageFront);

        } catch (Exception e) {
            // Manejo de cualquier otra excepción inesperada
            System.err.println("Error inesperado: " + e.getMessage());
            // Aquí también puedes agregar más lógica para manejar el error
        }
    }


    @MessageMapping("{roomId}/getMessages")
    public void getRoomMessages(@DestinationVariable String roomId, @Payload int numMsg) throws Exception {
        try {
            // Buscar el ID de la sala en la base de datos
            Optional<String> roomBDDId = this.roomService.getRoomIdByRoomName(roomId);

            if (roomBDDId.isPresent()) {
                String roomBDD = roomBDDId.get();
                
                // Obtener los últimos numMsg mensajes del servicio
                List<ChatMessage> ultimosMensajes = this.messageService.getLastMessages(roomBDD, numMsg);

                // Enviar los mensajes al cliente
                for (ChatMessage msg : ultimosMensajes) {
                    // Obtener información adicional para el mensaje
                    Optional<Room> roomOptional = roomService.getById(msg.getRoomId());
                    Optional<User> userOptional = userService.getById(msg.getSenderId());

                    if (roomOptional.isPresent() && userOptional.isPresent()) {
                        ChatMessageFront mensajeRecuperado = new ChatMessageFront(
                            roomOptional.get().getRoomName(),
                            userOptional.get().getUsername(),
                            msg.getContent(),
                            msg.getTimestamp()
                        );

                        // Enviar el mensaje al canal correspondiente
                        this.messageTemplate.convertAndSend("/topic/" + roomId, mensajeRecuperado);
                    } else {
                        // Manejo del caso en que la sala o el usuario no se encuentran
                        System.err.println("Información incompleta para el mensaje: sala o usuario no encontrados.");
                    }
                }

                System.out.println("Últimos " + numMsg + " mensajes enviados a /topic/" + roomId);
            } else {
                // Manejo del caso en que la sala no se encuentra
                System.err.println("Sala con nombre " + roomId + " no encontrada.");
            }
        } catch (Exception e) {
            // Manejo de cualquier otra excepción inesperada
            System.err.println("Error al obtener los mensajes: " + e.getMessage());
            throw new Exception("Error al obtener los mensajes: " + e.getMessage(), e);
        }
    }

    @MessageMapping("/{roomId}/files")
    public void handleFile(@DestinationVariable String roomId, @Payload FileMessageFront file) {

        // Aquí manejar el archivo (guardar, procesar, etc.)
        System.out.println("Archivo recibido: " + file.getFilename() + " en la sala: " + roomId);

        // Enviar de vuelta
        this.messageTemplate.convertAndSend("/topic/" + roomId, file);
    } 


}
