import { Injectable } from '@angular/core';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { ChatMessage } from '../models/chat-message';
import { AuthMessage } from '../models/auth-message';

@Injectable({
  providedIn: 'root'
})

export class WebSocketService {
  
  private stompClient: any 

  constructor() { 
    this.initConnectionSocket();
  }

  initConnectionSocket(){
    
    var url = 'http://localhost:8080/socket';
    const socket = new SockJS(url);
    this.stompClient = Stomp.over(socket);

    this.stompClient.connect({}, () => {
      console.log('Connected to WebSocket endpoint!');
      // Connection established, handle subscriptions or message sending here
    }, (error: Error) => {
      console.error('WebSocket connection error:', error);
      // Handle connection errors gracefully
    });

    // var client = Stomp.over(function(){
    //   return new WebSocket('ws://localhost:8080/chatSocket')
    // });
    // this.stompClient = client;

  }

  loginService(loginMessage: AuthMessage): string{
    let responseMessage: string = '';
    this.stompClient.send('/auth/login',{}, JSON.stringify(loginMessage));
    console.log("Mensaje enviado");

    this.stompClient.subscribe('/topic/auth/response', (messages: any) => {
      console.log("Mensaje Recibido");
      const message = JSON.parse(messages.body);
      console.log(message.body);
      message.body = message.body.toString();
      responseMessage = message;
    }, (error: Error) => {
      console.error("Subscription error:", error);
    });
    
    return responseMessage;
  }

  registryService(registerMessage: AuthMessage){
    this.stompClient.send(`/app/register`,{}, JSON.stringify(registerMessage));
  }

  joinRoom(roomId: string){
    this.stompClient.connect({}  , ()=>{
      this.stompClient.subscribe(`/topic/${roomId}` , (messages: any) => {
        const message = JSON.parse(messages.body);
        console.log('Message received ',message);
      }) //revisarWEBSOCKETCOntroller 
    })
  }
  
  sendMessage(roomId :string, chatMessage: ChatMessage){
    this.stompClient.send(`/app/chat/${roomId}`,{}, JSON.stringify(chatMessage));
    } 

}