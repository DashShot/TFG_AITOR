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
    const responseMessagePromise = new Promise<string>( (resolve, reject) => {
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
    return responseMessagePromise;
  }
  
}

export function mySocketFactory() {
  return new SockJS('http://localhost:8080/socket');
}
