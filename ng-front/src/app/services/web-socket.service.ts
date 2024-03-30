import { Injectable } from '@angular/core';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { AuthMessage } from  '../message-models/auth-message';
import { ChatMessage } from '../message-models/chat-message';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})

export class WebSocketService {
  
  private stompClient: any 
  private messageSubject: BehaviorSubject<ChatMessage[]> = new BehaviorSubject<ChatMessage[]>([]);

  constructor() { 
    this.initConnectionSocket();
  }

  initConnectionSocket(){
    
    // var url = 'http://localhost:8080/socket';
    //const socket = new SockJS(url); //factory
    //this.stompClient = Stomp.over(socket);
    
    this.stompClient = Stomp.over(mySocketFactory);

    this.stompClient.connect({}, () => {
      console.log('Connected to WebSocket endpoint!');
      // Connection established, handle subscriptions or message sending here
    }, (error: Error) => {
      console.error('WebSocket connection error:', error);
      // Handle connection errors gracefully
    });

  }

  loginService(loginMessage: AuthMessage): Promise<string> {
    return new Promise<string>( (resolve, reject) => {
      this.stompClient.send('/app/auth/login', {}, JSON.stringify(loginMessage));
      console.log("Mensaje enviado");
  
      this.stompClient.subscribe('/topic/auth/response', (messages: any) => {
        console.log("Mensaje Recibido");
        const message = messages;
        console.log(message);
        const messageString = new TextDecoder().decode(message._binaryBody);
        console.log(messageString);
        resolve(messageString); // Resolve the promise with the message
      }, (error: Error) => {
        reject(error); // Reject the promise with an error
      });
    });
  }
  
  listRooms(): Promise<Array<string>> {
    return new Promise<Array<string>>((resolve, reject) => {
      this.stompClient.send('/app/listRooms', {}, '{}');
      this.stompClient.subscribe('/topic/listRooms', (messages: any) => {
        try {
          console.log("Rooms Recibidas");
          console.log(messages);

          const messageString = new TextDecoder().decode(messages._binaryBody);
          console.log(messageString)
          
          // Parse the JSON string as a JavaScript object
          const roomsList = JSON.parse(messageString);
          console.log(roomsList);

          // Ensure roomsList is an array of strings
          if (!Array.isArray(roomsList) || !roomsList.every((room) => typeof room === 'string')) {
            throw new Error('Received data is not a valid string array');
          }
          resolve(roomsList); // Resolve the promise with the array of rooms
        } catch (error) {
          reject(error); // Reject the promise if parsing or validation fails
        }
      }, (error: Error) => {
        reject(error); // Reject the promise if subscription fails
      });
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


  getMessages(room: String): Promise<Array<string>> {
    return new Promise<Array<string>>((resolve, reject) => {
      this.stompClient.send('/app/getMessages', {}, JSON.stringify(room));
      this.stompClient.subscribe('/topic/getMessages', (messages: any) => {
        try {
          console.log("Mensages Recibidos");
          console.log(messages);

          const messageString = new TextDecoder().decode(messages._binaryBody);
          console.log(messageString)
          
          // Parse the JSON string as a JavaScript object
          const roomMessages = JSON.parse(messageString);
          console.log(roomMessages);

          // Ensure roomsList is an array of strings
          if (!Array.isArray(roomMessages) || !roomMessages.every((msg) => typeof msg === 'string')) {
            throw new Error('Received data is not a valid string array');
          }
          resolve(roomMessages); // Resolve the promise with the array of rooms
        } catch (error) {
          reject(error); // Reject the promise if parsing or validation fails
        }
      }, (error: Error) => {
        reject(error); // Reject the promise if subscription fails
      });
    });
  }

  sendMessage(message: ChatMessage): Promise<string>{
    return new Promise<string>( (resolve, reject) => {
      this.stompClient.send('/app/sendMessage', {}, JSON.stringify(message));
      console.log("Mensaje enviado");
  
      this.stompClient.subscribe('/topic/sendMessage', (response: any) => {
        console.log("Mensaje Recibido");
        const response2 = response;
        console.log(response2);
        const response3 = new TextDecoder().decode(response._binaryBody);
        console.log(response3);
        resolve(response3); // Resolve the promise with the message
      }, (error: Error) => {
        reject(error); // Reject the promise with an error
      });
    });
  }

  getMessageSubject(){
    return this.messageSubject.asObservable();
  }

}

export function mySocketFactory() {
  return new SockJS('http://localhost:8080/socket');
}
