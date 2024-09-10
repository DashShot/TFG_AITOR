package com.webchat_agc.controllers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final RoomService roomService;
    
    @Autowired
    private final ChatMessageService messageService;

    @Autowired
    private final UserService userService;


    

    @MessageMapping("/{roomId}")
    public void roomMessageHandler(@DestinationVariable String roomId, @Payload ChatMessageFront chatMessageFront) {
        System.out.println("MENSAJE RECIBIDO EN LA ROOM: " + roomId);
        System.out.println(chatMessageFront.getUsername() + "En la " + chatMessageFront.getRoom());
        
        // //GUARDAR EN BDD

        this.messageTemplate.convertAndSend("/topic/"+roomId,chatMessageFront);
    } 

    @MessageMapping("/{roomId}/files")
    public void handleFile(@DestinationVariable String roomId, @Payload FileMessageFront file) {

        // Aquí puedes manejar el archivo (guardar, procesar, etc.)
        System.out.println("Archivo recibido: " + file.getFilename() + " en la sala: " + roomId);
        // Enviar una respuesta a la sala si es necesario
        this.messageTemplate.convertAndSend("/topic/" + roomId, file);
    } 


    @MessageMapping("/getMessages")
    public void getRoomMessages(String roomName) throws Exception {
        List<String> messagesRoomList = new ArrayList<>();
        for (ChatMessage msg : this.roomService.getByName(roomName).getChatMessages()) {
            messagesRoomList.add("From: " + msg.getSender().getUsername() + " Content: " + msg.getContent() + " Room: " + msg.getRoom().getRoomName() + " Date: " + msg.getTimestamp());
        }

        System.out.println(messagesRoomList);

        this.messageTemplate.convertAndSend("/topic/getMessages",messagesRoomList);
        System.out.println("Message sent to /topic/getMessages");
    }

    //IMPLEMENTACIÓN POR  CHUNKS
    // @MessageMapping("/{roomId}/files")
    // public void handleFile(@DestinationVariable String roomId, @Payload Map<String, Object> chunkMessage) {
    //     String chunk = (String) chunkMessage.get("chunk");
    //     int chunkIndex = (int) chunkMessage.get("chunkIndex");
    //     int totalChunks = (int) chunkMessage.get("totalChunks");
    
    //     // Guardar el fragmento temporalmente (por ejemplo, en memoria o en disco)
    //     saveChunk(chunk, chunkIndex);
    
    //     // Si todos los fragmentos han sido recibidos, reconstruir el objeto
    //     if (chunkIndex == totalChunks - 1) {
    //         String largeObjectString = reassembleObject(totalChunks);
    //         // Aquí puedes convertir la cadena de vuelta al objeto original
    //         // y procesarlo según sea necesario
    //         System.out.println("Objeto grande recibido completamente: " + largeObjectString);

    //         this.messageTemplate.convertAndSend("/topic/"+ roomId, largeObjectString);
    //     }
    // }

    // private Map<Integer, String> chunkStorage = new HashMap<>();

    // // Guardar cada fragmento temporalmente en un mapa en memoria
    // public void saveChunk(String chunk, int chunkIndex) {
    //     // Almacenar el fragmento en el mapa, usando el índice como clave
    //     chunkStorage.put(chunkIndex, chunk);
    //     System.out.println("Guardando fragmento: " + chunkIndex);
    // }

    // public String reassembleObject(int totalChunks) {
    //     StringBuilder largeObject = new StringBuilder();
    
    //     // Recorrer los fragmentos en orden y reconstruir el objeto completo
    //     for (int i = 0; i < totalChunks; i++) {
    //         if (chunkStorage.containsKey(i)) {
    //             largeObject.append(chunkStorage.get(i));

                
    //         } else {
    //             throw new IllegalStateException("Falta un fragmento en el índice " + i);
    //         }
    //     }
    
    //     // Limpiar el almacenamiento temporal después de reensamblar
    //     chunkStorage.clear();
    
    //     System.out.println("Objeto reconstruido con éxito");
    //     return largeObject.toString();
    // }



    // @MessageMapping("/sendMessage")
    // public void sendMessage(@Payload ChatMessageFront chatMessageFront) throws  UsernameNotFoundException{
    //     System.out.println(chatMessageFront.getContent()+" --- Prueba --- "+chatMessageFront.toString());
    //     String response = "";

    //     Room room = roomService.getByName(chatMessageFront.getRoom());
    //     User user = userService.getByUsername(chatMessageFront.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
    //     ChatMessage chatMessage = new ChatMessage(chatMessageFront.getContent(), new Date(), user, room );
    //     System.out.print(chatMessage.getContent());

    //     room.addChatMessage(chatMessage);

    //     messageService.saveChatMessage(chatMessage);
    //     roomService.saveRoom(room);
    //     response = "Mensaje creado y enviado correctamente";
    //     this.messageTemplate.convertAndSend("/topic/sendMessage",response);
    //     System.out.println("Message sent to /topic/sendMessages");
    // }

    

    
}
