import { Injectable } from '@angular/core';


import { Client } from '@stomp/stompjs';


import { AuthMessage } from  '../message-models/auth-message';
import { ChatMessage } from '../message-models/chat-message';
import SockJS from 'sockjs-client';


@Injectable({
  providedIn: 'root'
})

export class WebSocketService {
  
  private stompClient: Client | undefined = undefined; 


 // private messageSubject: BehaviorSubject<ChatMessage[]> = new BehaviorSubject<ChatMessage[]>([]);

  constructor() { 
    this.initConnectionSocket();
    
  }

   initConnectionSocket(){
    console.log("Creando Cliente.....")

    this.stompClient = new Client({
      webSocketFactory: mySocketFactory,
      debug: function (str) {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
    });
    this.stompClient.onConnect = function (){
      console.log("CLIENTE CREADOO")
    }
    if(this.stompClient.connected) console.log("STOMP CONNECTED");
    //this.stompClient.deactivate()
    this.stompClient.activate()
    

  }

  loginService(loginMessage: AuthMessage): Promise<string> {
    return new Promise<string>((resolve, reject) => {
      
    if (this.stompClient){
      
        this.stompClient.publish({ destination: '/app/auth/login', body: JSON.stringify(loginMessage) });
        console.log("Mensaje enviado");
    
        const subscription = this.stompClient.subscribe('/topic/auth/response', (messages: any)  => {
          console.log("Respuesta a login recivida");
          console.log(messages);
          
          try {
            const messageString =(messages.body);
            console.log(messageString);
            resolve(messageString); // Resolve the promise with the message
          } catch (error) {
            console.error('Error decoding message:', error);
            reject(error); // Reject the promise with the error
          } finally {
            subscription.unsubscribe(); // Unsubscribe to avoid memory leaks
          }
       });
      }
    });
    }
    

  
  listRooms(): Promise<Array<string>> {
    return new Promise<Array<string>>((resolve, reject) => {
    if(this.stompClient){
      this.stompClient.publish({ destination: '/app/listRooms' });
      console.log("Mensaje enviado para listar salas");
  
      const subscription = this.stompClient.subscribe('/topic/listRooms', (messages: any) => {
        console.log("Lista de salas recibida");
        console.log(messages);
  
        try {
          const messageString = new TextDecoder().decode(messages._binaryBody);
          console.log(messageString);
  
          // Parse the JSON string and validate the data structure
          const roomsList: Array<string> = JSON.parse(messageString);
          if (!Array.isArray(roomsList) || !roomsList.every((room) => typeof room === 'string')) {
            throw new Error('Received data is not a valid string array');
          }
  
          resolve(roomsList); // Resolve with the list of rooms
        } catch (error) {
          console.error('Error obtaining room list:', error);
          reject(error); // Reject with the error
        } finally {
          subscription.unsubscribe(); // Unsubscribe to avoid memory leaks
        }
      });
    }
    
      
    });
  }
  

  // joinRoom(roomId: string) {
  //   this.stompClient.connect({}, ()=>{
  //     this.stompClient.subscribe(`/topic/${roomId}`, (messages: any) => {
  //       const messageContent = JSON.parse(messages.body);
  //       const currentMessage = this.messageSubject.getValue();
  //       currentMessage.push(messageContent);

  //       this.messageSubject.next(currentMessage);

  //     })
  //   })
  // }


  getMessages(room: string): Promise<Array<string>> {
    return new Promise<Array<string>>((resolve, reject) => {
      if(this.stompClient){
      this.stompClient.publish({destination: '/app/getMessages', body: JSON.stringify(room)});
      console.log("Mensaje enviado para obtener mensajes de la sala", room);
  
      const subscription = this.stompClient.subscribe('/topic/getMessages', (messages: any) => {
        console.log("Mensajes de la sala recibidos");
        console.log(messages);
  
        try {
          const messageString = new TextDecoder().decode(messages._binaryBody);
          console.log(messageString);
  
          // Parse the JSON string and validate the data structure
          const roomMessages: Array<string> = JSON.parse(messageString);
          if (!Array.isArray(roomMessages) || !roomMessages.every((msg) => typeof msg === 'string')) {
            throw new Error('Received data is not a valid string array');
          }
  
          resolve(roomMessages); // Resolve with the list of messages
        } catch (error) {
          console.error('Error obtaining messages:', error);
          reject(error); // Reject with the error
        } finally {
          subscription.unsubscribe(); // Unsubscribe to avoid memory leaks
        }
      });
    }
    });
  }
  

  sendMessage(message: ChatMessage): Promise<string>{
    return new Promise<string>( (resolve) => {
      if(this.stompClient)
      this.stompClient.publish({destination: '/app/sendMessage',body: JSON.stringify(message)});
      console.log("Mensaje enviado");
      resolve("Mensaje Enviado");
    });
  }
}

  // getMessageSubject(){
  //   return this.messageSubject.asObservable();
  // }



export function mySocketFactory() {
  return new SockJS('http://localhost:8080/socket');
}
