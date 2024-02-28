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
    // var url = "http://localhost:8080/chatSocket";
    // const socket = new SockJS(url);
    // this.stompClient = Stomp.over(socket);

    var client = Stomp.over(function(){
      return new WebSocket('ws://localhost:8080/chatSocket')
    });

  }

  loginService(loginMessage: AuthMessage): string{
    let responseMessage: string = '';
    this.stompClient.send(`/app/login`,{}, JSON.stringify(loginMessage));
     this.stompClient.subscribe('/topic/response', (message: any) => {
       console.log(message.body);
       message.body = message.body.toString();
       responseMessage = message.body.toString();
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