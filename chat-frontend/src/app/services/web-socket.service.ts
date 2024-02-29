import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import SockJS from 'sockjs-client';
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

    var url = "http://localhost:8080/chatSocket";
    const socket = new SockJS(url);
    this.stompClient = Stomp.over(socket);
    
    // var client = Stomp.over(function(){
    //   return new WebSocket('chatSocket://localhost:8080/chatSocket')
    // });
  }

  loginService(loginMessage: AuthMessage): string{
    console.log("COntectando con web socket:")
    let responseMessage: string = '';
    const ws = this.stompClient;

    this.stompClient.connect({}, (frame: any) => {
      console.log("Connected: " + frame);
      this.stompClient.subscribe('/topic/response', (message: any) => {
        console.log(message.body);
        message.body = message.body.toString();
        responseMessage = message.body.toString();
      });
    })
     ws.send(`/app/login`,{}, JSON.stringify(loginMessage));
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