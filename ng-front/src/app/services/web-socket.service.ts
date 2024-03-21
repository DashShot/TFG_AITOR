import { Injectable } from '@angular/core';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { AuthMessage } from  '../message-models/auth-message';

@Injectable({
  providedIn: 'root'
})

export class WebSocketService {
  
  private stompClient: any 

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
}

export function mySocketFactory() {
  return new SockJS('http://localhost:8080/socket');
}
