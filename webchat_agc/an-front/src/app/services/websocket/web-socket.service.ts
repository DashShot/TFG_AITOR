import { inject, Injectable } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';
import { BehaviorSubject } from 'rxjs';
import SockJS from 'sockjs-client';
import { ChatMessage } from '../../message-models/chat-message';
import { HttpClient } from '@angular/common/http';
import { FileMessage } from '../../message-models/file-message';
import { environment } from '../../../environments/enviroment';

@Injectable({
  providedIn: 'root'
})


export class WebSocketService {

  private stompClient: Client | undefined =  undefined
  private http = inject(HttpClient)


  private messageSubject: BehaviorSubject<ChatMessage[]> = new BehaviorSubject<ChatMessage[]>([]);
  private roomSubscription: any;
  
  constructor() {}

  initConnectionSocket(){
    const cookie = this.getCookie("AuthToken"); // Obtener el valor de la cookie "AuthToken"
    console.log("Prueba   pre socket")
    this.stompClient = new Client({
      
      brokerURL: (environment.wsUrl),
      webSocketFactory: () => new SockJS(environment.wsUrl),
      connectHeaders: {
        Authorization: `Bearer ${cookie}` // Enviar el token JWT 
      },
      debug: function (str) {
        console.log(str);
      },
      onConnect: function (){
        console.log("CLIENTE CREADOO")
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 10000,
      heartbeatOutgoing: 10000,
      // Increase WebSocket Frame Size
      maxWebSocketChunkSize: 128 * 1024 * 1024 // Increase chunk size 

    });

    this.stompClient.activate();

    // Verificar conexión (puede necesitar un tiempo para conectarse)
    this.stompClient.onConnect = () => {
      console.log("STOMP CONNECTED");
    };
  }

  getCookie(name: string): string | null {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop()?.split(';').shift() || null;
    }
    return null;
}

  closeConnection() {
    if (this.stompClient) {
      this.stompClient.deactivate();
    }
  }

  getListRooms() {
    return this.http.get(`${environment.apiUrl}/listRooms`, { observe: 'response' });
  }

  createRoom(roomId: string) {
    return this.http.post(`${environment.apiUrl}/createRoom`, roomId);
  }

  joinRoom(roomId: string) {
    if (this.stompClient) {
      this.roomSubscription = this.stompClient.subscribe(`/topic/${roomId}`, (message: Message) => {
        const messageContent: ChatMessage = JSON.parse(message.body);
        const currentMessages = this.messageSubject.getValue();
        currentMessages.push(messageContent);
        this.messageSubject.next(currentMessages);
      });

      const newUser: ChatMessage = {
        room: roomId,
        content: `${localStorage.getItem("UserLogged ")} ha entrado en la sala`,
        username: 'Server',
        timeStamp: new Date()
      };
      this.stompClient.publish({ destination: `/app/${roomId}`, body: JSON.stringify(newUser) });
    }
  }

  leftRoom(roomId: string) {
    if (this.stompClient) {
      const disconnectUser: ChatMessage = {
        content: `${localStorage.getItem("UserLogged ")} ha abandonado la sala`,
        username: 'Server',
        room: roomId,
        timeStamp: new Date()
      };
      this.stompClient.publish({ destination: `/app/${roomId}`, body: JSON.stringify(disconnectUser) });

      if (this.roomSubscription) {
        this.roomSubscription.unsubscribe();
      }
    }
  }

  sendMessage(roomId: string, message: ChatMessage): Promise<string> {
    return new Promise<string>((resolve) => {
      if (this.stompClient) {
        this.stompClient.publish({ destination: `/app/${roomId}`, body: JSON.stringify(message) });
        console.log(`Mensaje enviado a /app/${roomId}`);
        resolve("Mensaje Enviado");
      }
    });
  }

  sendFile(roomId: string, file: FileMessage): Promise<string> {
    return new Promise<string>((resolve) => {
      if (this.stompClient) {
        this.stompClient.publish({ destination: `/app/${roomId}/files`, body: JSON.stringify(file) });
        console.log(`Archivo enviado a /app/${roomId}`);
        resolve("Archivo Enviado");
      }
    });
  }

  getMessageSubject() {
    return this.messageSubject.asObservable();
  }

  getMessages(roomId: string): Promise<string> {
    return new Promise<string>((resolve) => {
      if (this.stompClient) {
        this.stompClient.publish({ destination: `/app/${roomId}/getMessages`, body: '5' });
        console.log(`Mensaje enviado para obtener mensajes de la sala ${roomId}`);
        resolve("Mensajes pedidos");
      }
    });
  }
}